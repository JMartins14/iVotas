/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdeleicoes;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;


public class RmiServer extends UnicastRemoteObject implements ConnectionRMI{
    static Files fo = new Files();
    static Database db = new Database();
    static private int portUdp,otherPort;
    static private InetAddress address,otherAddress;
    private static HashMap<String,ConnectionClient> tcpservers;

    /**
     *
     * @throws RemoteException
     */
    public RmiServer() throws RemoteException{
        super();
  
    }

    /**
     * Come por ler do ficheiro de objetos,tenta conetar-se como RMI e cria a conexao UDP
     * @param args
     */
    public static void main(String args[]){
            tcpservers = new HashMap<>();
            try{
                if (fo.OpenReadMode("dados.bin")) {
                    db = (Database) fo.ReadObject();
                    fo.closeReadMode();

                }
            }
            catch (IOException | ClassNotFoundException ex) {
                System.out.printf("Não foi possível ler os dados do ficheiro! Exception found: %s\n",ex);
            }
             boolean primary = false;
            RmiServer h = null;
            try {
                h = new RmiServer();
            } catch (RemoteException ex) {
                System.exit(1);
            }

           try{
                    if(args.length!=4){
                        System.out.println("Number of arguments invalid! Rmi Server will close.");
                        System.exit(1);
                    }
                    address = InetAddress.getByName(args[0]);
                    portUdp = Integer.parseInt(args[1]);
                    otherPort = Integer.parseInt(args[2]);
                    otherAddress = InetAddress.getByName(args[3]);
                    Registry registry = LocateRegistry.getRegistry(otherAddress.getHostAddress(), 1099);
                    registry.lookup("RmiServer1");
                    System.out.println("[INFO]: Backup Server RMI is ready! ");
                    primary = false;
                } catch (NotBoundException | RemoteException ex) {
                    primary = true;
                    try {
                        Registry registry = LocateRegistry.createRegistry(1099);
                        registry.rebind("RmiServer1", h);
                    } catch (RemoteException ex1) {
                        System.out.println("The registry in port 1099 is already created. The program will close\n");
                        System.exit(1);
                    }
                    System.out.println("[INFO]: Primary Server RMI is ready! ");

                } catch (UnknownHostException ex) {
                    System.out.println("Unknow host Ip. The program will close");
                    System.exit(1);
                }
                UDPConnection conn = new UDPConnection(primary,address,portUdp,otherPort,otherAddress,h);
                Thread novathread = new Thread(conn);
                novathread.start();            
        }
    public synchronized HashMap<String,String> voteDetail(String ccnumber,String title){
        User user = search_user(Long.parseLong(ccnumber));
        CopyOnWriteArrayList<Election> elections;
        Vote vote;
        HashMap<String,String> values = new HashMap<>();
        if(user!=null){
            elections = searchVotedElections(user);
            Election election = search_election(title);
            if(elections.size()>0 && election!=null){
                if(elections.contains(election)){
                    vote = election.getVoters().get(user.getName());
                    if(vote!=null){
                        values.put("name",user.getName());
                        values.put("electionTitle", title);
                        values.put("department",vote.getDepartment());
                        values.put("date", new SimpleDateFormat("dd/MM/yyyy-HH:mm").format(vote.getDate()));
                        return values;
                    }
                }
            }
        }
        return null;
    }
    /**
     * Muda de RMI Backup para Primario
     * Le o ficheiro de objetos, cria o registry e cria uma nova conexão udp desta vez como primario
     * @param h
     */
    public void changeRmi(RmiServer h){
        try{
            if (fo.OpenReadMode("dados.bin")) {
                db = (Database) fo.ReadObject();
                fo.closeReadMode();
            }
        }
        catch (IOException | ClassNotFoundException ex) {
            System.out.printf("Não foi possível ler os dados do ficheiro! Exception found: %s\n",ex);
        }
        try {
            LocateRegistry.createRegistry(1099);
            Naming.rebind("RmiServer1", h);
            System.out.println("Primary Server RMI ready!");
            UDPConnection conn = new UDPConnection(true,address,portUdp,otherPort,otherAddress,h);
            Thread novathread = new Thread(conn);
            novathread.start();
        } catch (RemoteException|MalformedURLException re) {
            System.out.println("Error rebinding or wrong url. The program will close!");
            System.exit(1);
        }   
    
}
    private synchronized void UpdateFiles(){
        try{
            fo.OpenWriteMode("dados.bin");
            fo.WriteObject((Database)db);
            fo.closeWriteMode();
        }
        catch (IOException ex){         
            System.out.printf("Não foi possível escrever os dados no ficheiro! Exception found: %s\n",ex);
        }
    }

    /**
     *
     * @return lista de users
     */
    @Override
    public synchronized String list_users(){
        CopyOnWriteArrayList<User> users = db.getUsers();
        String toAdmin = String.format("type | list_users; item_count | %d;",users.size());
        for (int i = 0; i < users.size(); i++) {
            toAdmin = toAdmin.concat(String.format(" user_%d | %s;",(i+1),users.get(i).getName()));

        }
        return toAdmin;
    }

    /**
     * Procura eleicoes que ainda não tenho acabado e que o user tenha permissao para votar e adiciona-lo como votante
     * @param user - Username
     */
    public synchronized void putElectionsOnUser(User user){
        CopyOnWriteArrayList<Election> elections = db.getElections();
        Date in = new Date();
        LocalDateTime ldt = LocalDateTime.ofInstant(in.toInstant(), ZoneId.systemDefault());
        Date date = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
        for (int i = 0; i < elections.size(); i++) {
            if(date.before(elections.get(i).getEnd())){
                if(user.getCategory().equalsIgnoreCase("stud") || elections.get(i).getCategory().equals("conselhogeral")){
                        elections.get(i).addVoter(user);
                }
                else if(user.getDepartment().equalsIgnoreCase(elections.get(i).getDepartment()))
                    elections.get(i).addVoter(user);
            }            
        }
        db.setElections(elections);
        UpdateFiles();
    }

    /**
     * Procura nas mesas da eleicao, a mesa com o mesmo despartamento
     * @param election
     * @param department
     * @return table
     */
    @Override
    public synchronized Table search_table(Election election,String department){
        CopyOnWriteArrayList<Table> tables = election.getTables();
        for (int i = 0; i < tables.size(); i++) {
            if(tables.get(i).getDepartment().equalsIgnoreCase(department))
                return tables.get(i);
        }
        return null;
    }
    public synchronized User loginFacebook(String id){
        CopyOnWriteArrayList<User> users = db.getUsers();
        for (int i = 0; i < users.size(); i++) {
            if(users.get(i).isAssociated()){
                if(users.get(i).getFacebookId().equals(id)){
                    return users.get(i);
                }
            }
        }
        return null;

    }
    public synchronized boolean findFacebookId(String id){
        CopyOnWriteArrayList<User> users = db.getUsers();
        for (int i = 0; i < users.size(); i++) {
            if(users.get(i).isAssociated()){
                if(users.get(i).getFacebookId().equals(id)){
                    return true;
                }
            }
        }
        return false;

    }
    public synchronized boolean associateUser(User user,String name,String id) {
    	CopyOnWriteArrayList<User> users;
        if(!findFacebookId(id)){
            int index = indexOfUser(user);
            user.setFacebookName(name);
            user.setFacebookId(id);
            user.setAssociated(true);
            users = db.getUsers();
            if(index>=0)
                users.set(index, user);
            db.setUsers(users);
            UpdateFiles();
            return true;
        }
        return false;
    }
    public int indexOfUser(User user){
        for (int i = 0; i < db.getUsers().size(); i++) {
            if(db.getUsers().get(i).getCcnumber()==user.getCcnumber()){
                return i;
            }
                
        }
        return -1;
    }
    /**
     * Procura user atraves do nome dele
     * @param user
     * @return user
     */
    public synchronized User search_user(String user){
        CopyOnWriteArrayList<User> users = db.getUsers();
        for (int i = 0; i < users.size(); i++) {
            if(users.get(i).getName().replaceAll(" ","").equalsIgnoreCase(user.replaceAll(" ","")))
                return users.get(i);
        }
        return null;
    }

    /**
     * Procura user atraves do id dele
     * @param user_id
     * @return user
     */
    public synchronized User search_user(int user_id){
        CopyOnWriteArrayList<User> users = db.getUsers();
        for (int i = 0; i < users.size(); i++) {
            if(users.get(i).getId() == user_id)
                return users.get(i);
        }
        return null;
    }

    /**
     * Procura user atraves do numero do CC
     * @param ccnumber
     * @return user
     */
    public synchronized User search_user(long ccnumber){
        CopyOnWriteArrayList<User> users = db.getUsers();
        for (int i = 0; i < users.size(); i++) {
            if(users.get(i).getCcnumber() == ccnumber)
                return users.get(i);
        }
        return null;
    }

    /**
     * Registra user
     *  Variaveis unicas:
     *          username
     *          ccnumber
     *  Outras
     *          Categoria
     *          Data de validade do CC
     *          Password
     *          Departamento
     *          Endereco de Morada
     *          Telefone
     *
     * @param values
     * @return true em caso de sucessos
     */
    @Override
        public synchronized boolean register(HashMap<String,String> values){
            Date date;
            SimpleDateFormat df = new SimpleDateFormat("dd/mm/yyyy");
            try{
                if(values.containsKey("category") && values.containsKey("date") && values.containsKey("password") && values.containsKey("department") && values.containsKey("address") && values.containsKey("telephone") && values.containsKey("ccnumber") && values.containsKey("username")){
                    date = df.parse(values.get("date"));
                    if(search_user(values.get("username"))!=null)
                        return false;
                    try{
                        if(search_user(Long.parseLong(values.get("ccnumber")))!=null)
                            return false;
                    }catch(NumberFormatException e){
                        return false;
                    }
                    User newUser = new User(values.get("category"),values.get("password"),values.get("department").toLowerCase(),values.get("address"),date,Long.parseLong(values.get("telephone")),Long.parseLong(values.get("ccnumber")),values.get("username"),db.getIds_users());
                    db.addId_users();
                    CopyOnWriteArrayList<String> departments = db.getDepartments();
                    if(departments.contains(values.get("department").toLowerCase()) && db.getCategories().contains(values.get("category"))){
                        CopyOnWriteArrayList<User> users = db.getUsers();
                        putElectionsOnUser(newUser);
                        users.add(newUser);
                        db.setUsers(users);
                        UpdateFiles();
                        return true;
                    }
                    else
                        return false;
                }
                else
                    return false;
                
            } catch (ParseException | NumberFormatException ex) {
                return false;
            }
            
        }

    /**
     *  Lista o Numero de Votos das mesas de uma determinada eleicao
     *      Variaveis Entrada:
     *          Id da eleicao
     * @param values
     * @return String
     */
    @Override
    public synchronized String votersOnElection(HashMap<String,String> values){
        if(values.containsKey("election_id")){
            int id;
            try{
                id = Integer.parseInt(values.get("election_id"));
            }catch(NumberFormatException e){
                return null;
            }
            Election election = search_election(id);
            if(election == null)
                return null;
            CopyOnWriteArrayList<Table> tables = election.getTables();
            String toAdmin = String.format(":: Election %s::\n",election.getTitle());
            for (int i = 0; i < tables.size(); i++) {
                toAdmin = toAdmin.concat(String.format("%s: %d Votes\n",tables.get(i).getDepartment(),tables.get(i).getNumVotes()));
            }
            return toAdmin;
        }
        return null;
    }

    /**
     * Adiciona departamento
     * @param department
     * @return true em caso de sucesso
     */
    @Override
    public synchronized boolean addDepartment(String department){
            CopyOnWriteArrayList<String> departments = db.getDepartments();
            if(!departments.contains(department) && department != null){
                departments.add(department);
                db.setDepartments(departments);
                UpdateFiles();
                return true;
            }
            else
                return false;
        }
    
    /**
     * Altera departamento
     * @param department
     * @param newname
     * @return true em caso de sucesso
     */
    @Override
    public synchronized boolean editDepartment(String department,String newname){
            CopyOnWriteArrayList<String> departments = db.getDepartments();
            if(departments.contains(department) && department != null && newname != null){
                if(!departments.contains(newname)){
                    departments.set(departments.indexOf(department),newname);
                    db.setDepartments(departments);
                    UpdateFiles();
                    return true;
                }else
                    return false;
                
                    
            }
            else
                return false;
        }
    
    /**
     * Lista todos os departamentos
     * @return String
     */
    @Override
    public synchronized String listDepartments(){
        CopyOnWriteArrayList<String> departments = db.getDepartments();
        String toAdmin = String.format("type | list_departments; item_count | %d;",departments.size());
        for (int i = 0; i < departments.size(); i++) {
            toAdmin = toAdmin.concat(String.format(" department_%d | %s;",(i+1),departments.get(i)));
        }
        return toAdmin;
    }

    /**
     * Remove departamento
     * @param department - Nome do departamento
     * @return
     */
    @Override
    public synchronized boolean removeDepartment(String department){
            CopyOnWriteArrayList<String> departments = db.getDepartments();
            if(departments.remove(department)){
                db.setDepartments(departments);
                UpdateFiles();
                return true;
            }
            else
                return false;
        }

    /**
     * Lista todas as listas de candidatos
     *  Variaveis Entrada:
     *     Id ou Titulo da Eleicao
     *     Tipo da Lista (stud,doc ou func)
     * @param values
     * @return
     */
    @Override
    public synchronized String list_CandidatesList(HashMap<String,String> values){
        if((values.containsKey("election_id") || values.containsKey("election")) && values.containsKey("typeList")){
            Election election = null;
            if(values.containsKey("election"))
                election = search_election(values.get("election"));
            else if(values.containsKey("election_id")){
                try{
                    election = search_election(Integer.parseInt(values.get("election_id")));
                }catch(NumberFormatException e){
                    return null;
                }
            }
            else
                return null;
            CopyOnWriteArrayList<CandidatesList> lists = new CopyOnWriteArrayList<>();
            if(election == null)
                return null;
            switch(values.get("typeList")){
                case "stud":
                    lists = election.getTotalCandidatesStud();
                    break;
                case "doc":
                    lists = election.getTotalCandidatesDoc();
                    break;
                case "func":
                    lists = election.getTotalCandidatesFunc();
                    break;
                default:
                    return null;
            }
            String toAdmin = String.format("type | list_candidateslists; item_count | %d;",lists.size());

            for (int i = 0; i < lists.size(); i++) {
                toAdmin = toAdmin.concat(String.format(" list_%d_name | %s;",(i+1),lists.get(i).getName()));
            }
            return toAdmin;
        }
        return null;
    }

    /**
     *  Procura Lista de Candidatos
     * @param election
     * @param listname
     * @param typeList
     * @return CandidatesList
     */
    public synchronized CandidatesList searchCandidatesList(Election election,String listname,String typeList){
        CopyOnWriteArrayList<CandidatesList> lists = new CopyOnWriteArrayList<>();
        if(election==null)
            return null;
        switch(typeList){
            case "stud":
                lists = election.getTotalCandidatesStud();
                break;
            case "doc":
                lists = election.getTotalCandidatesDoc();
                break;
            case "func":
                lists = election.getTotalCandidatesFunc();
                break;
            default:
                return null;
        }
        for (int i = 0; i < lists.size(); i++) {
            if(lists.get(i).getName().replaceAll(" ","").equalsIgnoreCase(listname.replaceAll(" ","")))
                return lists.get(i);
        }
        return null;
    }
    
    /**
     * Cria Lista de Candidatos
     * Variaveis Unicas
     *      Nome da Lista - name
     * Outras Variaveis
     *      Num de Candidatos
     *      Nome de cada candidato
     *      Titulo ou Id da Eleicao
     *      Tipo de Lista (stud,doc ou func)
     * @param values
     * @return true em caso de sucesso
     */
    @Override
    public synchronized boolean createList(HashMap<String,String> values){
            CopyOnWriteArrayList<String> candidates = new CopyOnWriteArrayList<>();
            String name;
            CandidatesList list;
            if(values.containsKey("numcandidates")){
                try{
                    for (int i = 0; i < Integer.parseInt(values.get("numcandidates")); i++) {
                        name = String.format("candidate_%d_name",i);
                        if(values.containsKey(name))
                            candidates.add(values.get(name));
                        else
                            return false;
                    }
                }catch(NumberFormatException e){
                    return false;
                }
            }
            else
                return false;
            Election election = null;
            if(values.containsKey("election"))
                election = search_election(values.get("election"));
            else if(values.containsKey("election_id"))
                try{
                    election = search_election(Integer.parseInt(values.get("election_id")));
                }catch(NumberFormatException e){
                    return false;
                }
            if(election == null)
                return false;
            if(values.containsKey("name") && values.containsKey("typeList")){
                if(searchCandidatesList(election,values.get("name"),values.get("typeList"))!=null)
                    return false;
                list = new CandidatesList(values.get("typeList"),values.get("name"),Integer.parseInt(values.get("numcandidates")),candidates);
                if(values.get("typeList").equals("stud") || election.getCategory().equals("conselhogeral")){
                    switch(values.get("typeList")){
                        case "doc":
                            election.addCandidatesDoc(list);
                            break;
                        case "stud":
                            election.addCandidatesStud(list);
                            break;
                        case "func":
                            election.addCandidatesFunc(list);
                            break;
                        default:
                            return false;
                    }    
                }
                else
                    return false;
                UpdateFiles();
                return true;
            }
            return false;
           
        }

    /**
     * Login
     *  Variaveis
     *      Username
     *      Password
     * @param values
     * @return user
     */
    @Override
        public synchronized User login(HashMap<String,String> values){
            CopyOnWriteArrayList<User> users = db.getUsers();
            if(values.containsKey("username") && values.containsKey("password")){
                for (int i = 0; i < users.size(); i++) {
                    if(users.get(i).getName().replaceAll(" ","").equalsIgnoreCase(values.get("username").replaceAll(" ",""))){
                        if(users.get(i).getPassword().equals(values.get("password"))){
                            return users.get(i);
                        }
                    }
                }
            }
            return null;
        }
        
    /**
     * Cria Eleicao
     * Variaveis Unicas
     *      Titulo
     * Outras Variaveis
     *      Data de Inicio - dd/MM/YYYY-HH:mm
     *      Data de Fim- dd/MM/YYYY-HH:mm
     *      Categoria
     *      Descricao
     * 
     * @param values
     * @return
     */
    @Override
    public synchronized boolean create_election(HashMap<String,String> values){
        Date start,end;
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy-HH:mm");  
        try {

            if(values.containsKey("start") && values.containsKey("end") && values.containsKey("category") && values.containsKey("title") && values.containsKey("description")){
                start = df.parse(values.get("start"));
                end = df.parse(values.get("end"));
                Date in = new Date();
                LocalDateTime ldt = LocalDateTime.ofInstant(in.toInstant(), ZoneId.systemDefault());
                Date date = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
                if(date.after(end))
                    return false;
                if(start.after(end))
                    return false;
                Election election;
                if(search_election(values.get("title"))!=null)
                    return false;
                if(values.get("category").equals("nucleoestudantes") && values.containsKey("department"))
                    election = new Election(values.get("category"),values.get("department"),values.get("title"),values.get("description"),start,end,db.getIds_elections());
                else if(values.get("category").equals("conselhogeral"))
                    election = new Election(values.get("category"),values.get("title"),values.get("description"),start,end,db.getIds_elections());
                else
                    return false;
                db.addId_elections();
                election.addVoters(db.getUsers());
                CopyOnWriteArrayList<Election> elections = db.getElections();
                elections.add(election);
                db.setElections(elections);
                UpdateFiles();
            }
            else
                return false;
        } catch (ParseException ex) { // Tratar Exception
            return false;
        }
        return true;
    }

    @Override
    public synchronized CopyOnWriteArrayList<String> getPastElections(){
        CopyOnWriteArrayList<Election> elections = db.getElections();
        CopyOnWriteArrayList<String> titles = new CopyOnWriteArrayList<>();
        Date in = new Date();
        LocalDateTime ldt = LocalDateTime.ofInstant(in.toInstant(), ZoneId.systemDefault());
        Date date = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
        for (int i = 0; i < elections.size(); i++) {
            if(elections.get(i).getEnd().before(date))
                titles.add(elections.get(i).getTitle());
        }
        return titles;
    }
    
    /**
     * Consultar estatisticas de uma eleicao passada
     *  Variaveis Entrada
     *          Id da Eleicao
     * @param values
     * @return lista com detalhes
     */
    

    
    @Override
    public synchronized String getStatsElection(HashMap<String,String> values){
        if(values.containsKey("election_id")){
            Election election = search_election(Integer.parseInt(values.get("election_id")));
            if(election==null)
                return null;
            Date in = new Date();
            LocalDateTime ldt = LocalDateTime.ofInstant(in.toInstant(), ZoneId.systemDefault());
            Date date = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
            if(election.getEnd().after(date))
                return null;
            String toAdmin = "";
            int size,totalvotes;
            float number;
            toAdmin = toAdmin.concat(String.format("Title: %s\nDescription: %s\n\n::STUDENTS LISTS::\n",election.getTitle(),election.getDescription()));
            if(election.getTotalVotesStud()>0){
                totalvotes = election.getTotalVotesStud();
                for (int i = 0; i < election.getTotalCandidatesStud().size(); i++) {
                    size = election.getTotalCandidatesStud().get(i).getVotes().size();
                    number = (float)size/totalvotes;
                    toAdmin = toAdmin.concat(String.format("List %s: %d Votes - %.2f%%\n",election.getTotalCandidatesStud().get(i).getName(),election.getTotalCandidatesStud().get(i).getVotes().size(),number*100));
                }
                size = election.getWhiteVotesStud().size();
                number = (float)size/totalvotes;
                toAdmin = toAdmin.concat(String.format("White Votes: %d Votes - %.2f%%\n",election.getWhiteVotesStud().size(),number*100));
                size = election.getNullVotesStud().size();
                number = (float)size/totalvotes;
                toAdmin = toAdmin.concat(String.format("Null Votes: %d Votes - %.2f%%\n",election.getNullVotesStud().size(),number*100));
            }
            if(election.getCategory().equalsIgnoreCase("conselhogeral")){
                toAdmin = toAdmin.concat("\n::DOCENTS LISTS::\n");
                totalvotes = election.getTotalVotesDoc();

                if(election.getTotalVotesDoc()>0){
                    for (int i = 0; i < election.getTotalCandidatesDoc().size(); i++) {
                        size = election.getTotalCandidatesDoc().get(i).getVotes().size();
                        number = (float)size/totalvotes;
                        toAdmin = toAdmin.concat(String.format("List %s: %d Votes - %.2f%%\n",election.getTotalCandidatesDoc().get(i).getName(),election.getTotalCandidatesDoc().get(i).getVotes().size(),number*100));
                    }
                    size = election.getWhiteVotesDoc().size();
                    number = (float)size/totalvotes;
                    toAdmin = toAdmin.concat(String.format("White Votes: %d Votes - %.2f%%\n",election.getWhiteVotesDoc().size(),number*100));
                    size = election.getNullVotesDoc().size();
                    number = (float)size/totalvotes;
                    toAdmin = toAdmin.concat(String.format("Null Votes: %d Votes - %.2f%%\n",election.getNullVotesDoc().size(),number*100));
                }
                toAdmin = toAdmin.concat("\n::EMPLOYEES LISTS::\n");
                totalvotes = election.getTotalVotesDoc();

                if(election.getTotalVotesFunc()>0){
                    for (int i = 0; i < election.getTotalCandidatesFunc().size(); i++) {
                        size = election.getTotalCandidatesFunc().get(i).getVotes().size();
                        number = (float)size/totalvotes;
                        toAdmin = toAdmin.concat(String.format("List %s: %d Votes - %.2f%%\n",election.getTotalCandidatesFunc().get(i).getName(),election.getTotalCandidatesFunc().get(i).getVotes().size(),number*100));
                    }
                    size = election.getWhiteVotesDoc().size();
                    number = (float)size/totalvotes;
                    toAdmin = toAdmin.concat(String.format("White Votes: %d Votes - %.2f%%\n",election.getWhiteVotesFunc().size(),number*100));
                    size = election.getNullVotesDoc().size();
                    number = (float)size/totalvotes;
                    toAdmin = toAdmin.concat(String.format("Null Votes: %d Votes - %.2f%%\n",election.getNullVotesFunc().size(),number*100));
                }
            }    
            return toAdmin;
        }
        return null;
    }
    @Override
    public synchronized CopyOnWriteArrayList<String> votedElections(String username){
        User user = this.search_user(username);
        CopyOnWriteArrayList<Election> elec =  searchVotedElections(user);
    CopyOnWriteArrayList<String> titles = new CopyOnWriteArrayList<>();
    for (int i = 0; i < elec.size(); i++) {
        titles.add(elec.get(i).getTitle());                
    }
    return titles;
}
    /**
     * Procura eleicao atraves do id
     * @param id
     * @return Election
     */
    public synchronized Election search_election(int id){
        CopyOnWriteArrayList<Election> elections = db.getElections();
        for(int i = 0; i < elections.size(); i++) {
           if(elections.get(i).getId() == id){
              return elections.get(i);
           } 
        }
        return null;
    }

    /**
     * Procura Eleicao atraves do Titulo
     * @param title
     * @return Election
     */
    public synchronized Election search_election(String title){
        CopyOnWriteArrayList<Election> elections = db.getElections();
        for(int i = 0; i < elections.size(); i++) {
           if(elections.get(i).getTitle().replaceAll(" ","").equals(title.replaceAll(" ",""))){
              return elections.get(i);
           } 
        }
        return null;
    }
    
    //Retorna o que está numa lista de item (ex:listas de deputados)

    /**
     * Faz a contagem dos items
     * @param values
     * @return Array com os items
     */
    @Override
    public synchronized CopyOnWriteArrayList item_count(HashMap<String,String> values){
        CopyOnWriteArrayList<String> items = new CopyOnWriteArrayList();
        int count =Integer.parseInt(values.get("item_count"));
        System.out.println(count);
        System.out.println(values);
        for (int i = 0; i < count; i++) {
            String item = "item_"+i+"_name";
            String name = values.get(item);
            items.add(name);
        }
        System.out.println(items);
        return items;
    }

    /**
     * Altera propriedades pessoais do User
     *  Variaveis obrigatorias
     *      Nome ou Id do User
     * 
     * @param values
     * @return true em caso de sucesso
     */
    @Override    
    public synchronized boolean edit_user(HashMap<String,String> values){
        
        User user = null;
        if(values.containsKey("username")){
            user = search_user(values.get("username"));
        }
        else if(values.containsKey("user_id")){
            try{
                user = search_user(Integer.parseInt("user_id"));
            }catch(NumberFormatException e){
                return false;
            }
        }
        else
            return false;
        if(user == null)
            return false;
        if(values.containsKey("new_username"))
            user.setName(values.get("new_username"));
        if(values.containsKey("new_password"))
            user.setPassword(values.get("new_password"));
        if(values.containsKey("category")){
            if(db.getCategories().contains(values.get("category")))
                user.setCategory(values.get("category"));
        }
        if(values.containsKey("address"))
            user.setAddress(values.get("address"));
        if(values.containsKey("department")){
            if(db.getDepartments().contains(values.get("department")))
                user.setDepartment(values.get("department"));
        }
        if(values.containsKey("ccnumber")){
            try{
                if(search_user(Long.parseLong(values.get("ccnumber")))==null)
                    user.setCcnumber(Long.parseLong(values.get("ccnumber")));
                else 
                    return false;
            }catch(NumberFormatException e){
                return false;
            }
        }
        if(values.containsKey("telephone")){
            try{
                user.setTelephone(Long.parseLong(values.get("telephone")));
            }catch(NumberFormatException e){
                return false;
            }
        }
        if(values.containsKey("date")){
            try{
                SimpleDateFormat df =  new SimpleDateFormat("dd/mm/yyyy");
                Date date = df.parse(values.get("date"));   
                user.setCcexpirationdate(date);

            } catch (ParseException ex) {
                return false;
            }
        }
        return true;
    }

    /**
     * Edita eleicao
     *  Variaveis Obrigatorias
     *      Id ou Titulo da Eleicao
     * @param values
     * @return true em caso de sucesso
     */
    @Override
    public synchronized boolean edit_election(HashMap<String,String> values){
        SimpleDateFormat df =  new SimpleDateFormat("dd/MM/yyyy-hh:mm");
        Election election = null;
        if(values.containsKey("election"))
            election = search_election(values.get("election"));
        else if(values.containsKey("election_id")){
            try{
                election = search_election(Integer.parseInt(values.get("election_id")));
            }catch(NumberFormatException e){
                return false;
            }
        }
        else
            return false;
        if(election==null)
            return false;
        if(values.containsKey("description"))
            election.setDescription(values.get("description"));
        if(values.containsKey("title"))
            election.setTitle(values.get("title"));
        if(values.containsKey("start")){
            try {
                Date start = df.parse(values.get("start"));                
                election.setStart(start);
 
            } catch (ParseException ex) {
                return false;
            }
        }
        if(values.containsKey("end")){  
            try {
                Date end = df.parse(values.get("end"));                
                election.setEnd(end);
 
            } catch (ParseException ex) {
                return false;
            }
        }
        db.editElection(election);
        return true;
    }
    //Falta desbloquear terminal se user encontrado

    /**
     * Identificar user na mesa de voto
     * Variaveis de Entrada
     *      Numero de CC
     * @param values
     * @return true em caso de sucesso
     */
    @Override
    public synchronized boolean identify_user(HashMap<String,String> values){
         CopyOnWriteArrayList<User> users = db.getUsers();
         String ident = null;
         String[] val = {"ccnumber"};
         for (String s : val){
             for(String t : values.keySet()){
                 if(s.equals(t)){
                     ident = s;
                     break;
                }
             }
         }
         if(ident == null)
             return false;
        switch(ident){
            case "ccnumber":
                for (int i = 0; i < users.size(); i++) {
                    if(users.get(i).getCcnumber() == Long.parseLong(values.get(ident)))
                        return true;
                }
                break;
        }
             return false;
    }

    /**
     *  Retorna todas as eleicoes que foram votadas pelo user
     * @param user
     * @return
     */
    public synchronized CopyOnWriteArrayList<Election> searchVotedElections(User user){
        CopyOnWriteArrayList<Election> pastElections = new CopyOnWriteArrayList<>();
        CopyOnWriteArrayList<Election> elections = db.getElections();
        Vote vote;
        for (int i = 0; i < elections.size(); i++) {
            if(elections.get(i).getVoters().containsKey(user.getName()) && (vote=elections.get(i).getVoters().get(user.getName()))!=null){
                pastElections.add(elections.get(i));
            }
        }
        return pastElections;
    }

    /**
     * Consultar detalhes de um voto
     * Variaveis obrigatorias
     *      Nome do user ou Numero do CC
     * @param option
     * @param values
     * @return
     */
    @Override
    public synchronized String detailVote(int option,HashMap<String,String> values){
        User user;
        CopyOnWriteArrayList<Election> elections;
        String toAdmin;
        Election election;
        Vote vote;
        if(values.containsKey("username") || values.containsKey("ccnumber")){
            if(values.containsKey("username")){
                user = search_user(values.get("username"));
            }
            else if(values.containsKey("ccnumber")){
                try{
                    user = search_user(Long.parseLong(values.get("ccnumber")));
                }catch(NumberFormatException e){
                     return null;
                }
            }
            else
                return null;
           if(user !=null){
                elections = searchVotedElections(user);
                toAdmin = "::Detail_Vote::\n";
                if(option>0 && option <= elections.size()){
                    election = elections.get(option-1);
                    if(election.getVoters().containsKey(user.getName())){
                        vote = election.getVoters().get(user.getName());
                        toAdmin = toAdmin.concat(String.format("Name: %s\n",user.getName()));
                        toAdmin = toAdmin.concat(String.format("Election: %s\n",vote.getElection().getTitle()));
                        toAdmin = toAdmin.concat(String.format("Department: %s\n",vote.getDepartment()));
                        toAdmin = toAdmin.concat(String.format("Date: %s\n",vote.getDate().toString()));
                        return toAdmin;
                    }
                }
           }
        }
        return null;

    }

    /**
     * Lista todas as Eleicoes Votadas pelo user
     * Variaveis Obrigatorias:
     *      Nome do user ou Nº do CC
     *
     * @param values
     * @return
     */
    @Override
    public synchronized String listVotedElections(HashMap<String,String> values){
        User user;
        CopyOnWriteArrayList<Election> elections;
        String toAdmin = "";
        if(values.containsKey("username") || values.containsKey("ccnumber")){
            if(values.containsKey("username")){
                user = search_user(values.get("username"));
            }
            else if(values.containsKey("ccnumber")){
                try{
                    user = search_user(Long.parseLong(values.get("ccnumber")));
                }catch(NumberFormatException e){
                     return null;
                }
            }
            else
                return null;
           if(user !=null){
               elections = searchVotedElections(user);
               if(elections.isEmpty())
                   return null;
               toAdmin = toAdmin.concat(String.format("::List_Elections::\n"));
               for (int i = 0; i < elections.size(); i++) {
                    toAdmin = toAdmin.concat(String.format("%d) %s\n",(i+1),elections.get(i).getTitle()));
               }
               toAdmin = toAdmin.concat("\nChoice: ");
               return toAdmin;
           }
        }
        return null;
            
    }

    /**
     * Submeter voto na eleicao
     * @param user
     * @param election
     * @param option - opcao de voto
     * @param table -  mesa de voto
     * @return true em caso de sucesso
     */
    @Override
    public synchronized boolean submitVote(User user, Election election,int option,Table table){
        Date in = new Date();
        LocalDateTime ldt = LocalDateTime.ofInstant(in.toInstant(), ZoneId.systemDefault());
        Date date = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
        CopyOnWriteArrayList<CandidatesList> list = new CopyOnWriteArrayList<>();
        int category;
        Vote newVote;
        switch(user.getCategory()){
            case "stud":
                list = election.getTotalCandidatesStud();
                category = 0;
                break;
            case "doc":
                list = election.getTotalCandidatesDoc();
                category = 1;
                break;
            case "func":
                list = election.getTotalCandidatesFunc();
                category = 2;
                break;
            default:
                return false;
        }
        //se a opcao for uma das da lista
        if(date.after(election.getStart()) && date.before(election.getEnd())){
            if(option>0 && option <= list.size()){
                newVote = new Vote(list.get(option-1),election,date,table);
                list.get(option-1).addVote(newVote);
                switch(category){
                    case 0:
                        election.setCandidatesStud(list);
                        break;
                    case 1:
                        election.setCandidatesDoc(list);
                        break;
                    case 2:
                        election.setCandidatesFunc(list);
                        break;
                }
            }
                // se for voto branco
                else if(option==list.size()+1){
                    newVote = new Vote(null,election,date,table);
                switch (category) {
                    case 0:
                        election.addWhiteVoteStud(newVote);
                        break;
                    case 1:
                        election.addWhiteVoteDoc(newVote);
                        break;
                    default:
                        election.addWhiteVoteFunc(newVote);
                        break;
                }
                }
                //se for voto nulo
                else{
                    newVote = new Vote(null,election,date,table);
                switch (category) {
                    case 0:
                        election.addNullVoteStud(newVote);
                        break;
                    case 1:
                        election.addNullVoteDoc(newVote);
                        break;
                    default:
                        election.addNullVoteFunc(newVote);
                        break;
                }
                }
            switch (category) {
                case 0:
                    election.addToTotalVotesStud(); //adiciona 1 ao contador dos votos
                    break;
                case 1:
                    election.addToTotalVotesDoc();
                    break;
                case 2:
                    election.addToTotalVotesFunc();
                    break;
                default:
                    break;
            }
                election.updateVoters(user.getName(), newVote);
                table.addNumVote();
                election.editTable(table);
                db.editElection(election);
                UpdateFiles();
                return true;
            }
            return false;
    }
    
    /**
     * Mostra Informaçoes da Eleicao e todas as listas candidatas
     * @param user
     * @param election
     * @return
     */
    @Override
        public String showInfoElection(User user,Election election){
        String toClient = "";
        CopyOnWriteArrayList<CandidatesList> list = new CopyOnWriteArrayList<>();
        int i;
        toClient = toClient.concat(String.format("Title: " + election.getTitle() + "\n"));
        toClient = toClient.concat(String.format("Description: " + election.getDescription() + "\n"));
        toClient = toClient.concat(String.format("Start Date: " + election.getStart().toString() + "\n"));
        toClient = toClient.concat(String.format("End Date: " + election.getEnd().toString() + "\n"));
        toClient = toClient.concat("\n::Candidates::\n");
        //Os estudantes so podem votar em estudantes,...
        switch(user.getCategory()){
            case "stud":
                list = election.getTotalCandidatesStud();
                break;
            case "doc":
                list = election.getTotalCandidatesDoc();
                break;
            case "func":
                list = election.getTotalCandidatesFunc();
                break;
        }
        for(i=0;i<list.size();i++){
            toClient = toClient.concat((i+1) +") " + list.get(i).getName() + "\n");
        }
        toClient = toClient.concat(String.format((i+1) + ") White Vote\n" + (i+2) + ") Null Vote\n\n0) Quit\n\nChoice: "));
        return toClient;
    }
        
    /**
     * Procura todas as eleicoes em que um user pode votar
     * @param user
     * @return
     */
    @Override
    public CopyOnWriteArrayList<Election> searchPossibleElections(User user){
        boolean stud = user.getCategory().equals("stud");
        CopyOnWriteArrayList<Election> elections = db.getElections();
        CopyOnWriteArrayList<Election> possibleElections = new CopyOnWriteArrayList<>();
        int i;
        Date in = new Date();
        LocalDateTime ldt = LocalDateTime.ofInstant(in.toInstant(), ZoneId.systemDefault());
        Date date = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());      
        //Procura as eleições disponíveis para o user votar(Verifica datas e permissão para o user votar)
        for(i=0;i<elections.size();i++){
            if(elections.get(i).getStart().before(date) && date.before(elections.get(i).getEnd())){
                if((elections.get(i).getCategory().equals("conselhogeral") || stud) && elections.get(i).getVoters().containsKey(user.getName().replaceAll(" ","")) && elections.get(i).getVoters().get(user.getName().replaceAll(" ",""))==null){
                    possibleElections.add(elections.get(i));
                }
            }
        }
        return possibleElections;
    }
   
    /**
     * Lista todas as eleicoes
     * @return
     */
    @Override
    public synchronized String list_elections(){
        CopyOnWriteArrayList<Election> elections = db.getElections();
        String toAdmin = String.format("type | list_elections; item_count | %d;",elections.size());
        for (int i = 0; i < elections.size(); i++) {
            toAdmin = toAdmin.concat(String.format(" election_%d_title | %s; election_%d_id | %d;",(i+1),elections.get(i).getTitle(),(i+1),elections.get(i).getId()));
        }
        return toAdmin;
    }

    /**
     * Adiciona mesa de voto
     * Variaveis entrada
     *      Departamento
     *      Id da eleicao
     *      Username da Mesa
     *      Password da Mesa
     * @param values
     * @return true em caso de sucesso
     */
    @Override
    public synchronized boolean add_table(HashMap<String,String> values){
        int id;
        if(values.containsKey("department") && values.containsKey("election_id") && values.containsKey("username") && values.containsKey("password")){
            try{
                id = Integer.parseInt(values.get("election_id"));
                if (db.getDepartments().contains(values.get("department").toLowerCase())){
                    Table table = new Table(values.get("department").toLowerCase(),values.get("username"),values.get("password"));
                    for (int i = 0; i < db.getElections().size(); i++) {
                        if(db.getElections().get(i).getId() == id){
                            if(!db.getElections().get(i).getTables().contains(table) && (db.getElections().get(i).getCategory().equalsIgnoreCase("conselhogeral") ||db.getElections().get(i).getDepartment().equalsIgnoreCase(values.get("department")))){                               
                                db.getElections().get(i).getTables().add(table);
                                UpdateFiles();
                                return true;
                            }
                        }              
                    }
                }
            }
            catch(NumberFormatException e){
                return false;
            }
        }
        return false;
    }

    /**
     * Login da mesa
     * Variaveis Entrada
     *      Username da Mesa
     *      Password da Mesa
     * @param values
     * @param department
     * @return
     */
    @Override
    public synchronized boolean loginTable(HashMap<String,String> values,String department){
        Table table;
        if(values.containsKey("username") && values.containsKey("password")){
            for (int i = 0; i < db.getElections().size(); i++) {
                for (int j = 0; j <db.getElections().get(i).getTables().size(); j++) {
                    table = db.getElections().get(i).getTables().get(j);
                    if(table.getDepartment().equalsIgnoreCase(department)){
                        if(table.getUser().equalsIgnoreCase(values.get("username")) && table.getPassword().equals(values.get("password"))){
                            return true;
                        }
                    }                    
                }
            }
        }
        return false;
    }

    /**
     * Remover mesa de voto
     * Variaveis Entrada:
     *      Departamento
     *      Id da Eleicao
     * @param values
     * @return
     */
    @Override
    public synchronized boolean remove_table(HashMap<String,String> values){
        int id;
        if(values.containsKey("department") && values.containsKey("election_id")){
            id = Integer.parseInt(values.get("election_id"));
            for (int i = 0; i < db.getElections().size(); i++) {
                if(db.getElections().get(i).getId() == id)
                for (int j = 0; j < db.getElections().get(i).getTables().size(); j++) {
                    if(db.getElections().get(i).getTables().get(j).getDepartment().equalsIgnoreCase(values.get("department"))){
                        db.getElections().get(i).getTables().remove(j);
                        UpdateFiles();
                        return true;
                    }

                }
            }
        }
        return false;
    }

    /**
     * Lista todas as mesas de votos, dando informacao se estao up ou nao, juntamente com os terminais se estes estiverem on
     * @return
     */
    @Override
    public synchronized String list_tables(){
        String answer ="";
        CopyOnWriteArrayList<Election> elections = db.getElections();
        for (int i = 0; i < elections.size(); i++) {
            answer = answer.concat("\nElection: "+elections.get(i).getTitle()+"\n");
            CopyOnWriteArrayList<Table> tables = elections.get(i).getTables();
            for(int j = 0; j < tables.size(); j++) {
                answer = answer.concat("\t"+tables.get(j).getDepartment());
                if(tcpservers.containsKey(tables.get(j).getDepartment())){
                    if(tables.get(j).isOn(tcpservers.get(tables.get(j).getDepartment()))){
                        answer = answer.concat(" is UP\n");
                        //List Terminals
                        CopyOnWriteArrayList<String> terminals;
                        terminals = tables.get(j).getTerminals();
                        for(String t : terminals){
                            answer = answer.concat("\t\t"+t+ " is connected\n");
                        }
                    }
                    else{
                        answer = answer.concat(" is DOWN\n");
                        tcpservers.remove(tables.get(j).getDepartment());
                    }
                }
                else 
                    answer = answer.concat(" is DOWN\n");
            }
        } 
        return answer;
    }

    /**
     * Funcao que vai atualizando as mesas de voto vendo quais estao on
     * 
     * @param con
     * @param name
     * @return
     * @throws RemoteException
     */
    @Override
    public synchronized boolean subscribe(ConnectionClient con,String name) throws RemoteException{
        tcpservers.keySet().forEach((s) -> {
            try {
                tcpservers.get(s).print_on_server();
            }catch(RemoteException e){
                tcpservers.remove(s);
            }
        });
        for (int i = 0; i < db.getElections().size(); i++) {
            for (int j = 0; j <db.getElections().get(i).getTables().size(); j++) {
                if(db.getElections().get(i).getTables().get(j).getDepartment().equalsIgnoreCase(name)){
                    if(!tcpservers.containsKey(name.toLowerCase())){
                        tcpservers.put(name.toLowerCase(),con);
                        return true;
                    }
                }                    
            }
        }
      
        return false;
    } 

    /**
     *
     * @return
     */
    @Override
    public synchronized boolean isUp(){
        return true;
    } 
    
    public CopyOnWriteArrayList<String> getDepartments(){
        return db.getDepartments();
    }
    public CopyOnWriteArrayList<String> getElections(){
        CopyOnWriteArrayList<String> elect = new CopyOnWriteArrayList<>();
         for (int i = 0; i < db.getElections().size(); i++) {
            elect.add(db.getElections().get(i).getTitle());
        }
         return elect;
     }
    @Override
    public synchronized CopyOnWriteArrayList<String> getTitles(){
        CopyOnWriteArrayList<String> titles = new CopyOnWriteArrayList<>();
        CopyOnWriteArrayList<Election> elections = db.getElections();
 
        for (int i = 0; i < elections.size(); i++) {
            titles.add(elections.get(i).getTitle());
        }
        return titles;
    }
    public synchronized CopyOnWriteArrayList<String> getUsers(){
        CopyOnWriteArrayList<User> users = db.getUsers();
      CopyOnWriteArrayList<String> names = new CopyOnWriteArrayList<>();
        for (int i = 0; i < users.size(); i++) {
            names.add(users.get(i).getName());
        }
        return names;      
    }
    public synchronized boolean editUser(String user,String category, String department,String name, String password,long telephone,String address,long ccnumber,Date expiration){
        CopyOnWriteArrayList<User> users = db.getUsers();
        if(!user.equalsIgnoreCase(name) && search_user(name) != null)
                    return false;
        for (int i = 0; i < users.size(); i++) {
            if(user.equals(users.get(i).getName())){
                users.get(i).setAddress(address);
                users.get(i).setCategory(category);
                users.get(i).setName(name);
                users.get(i).setDepartment(department);
                users.get(i).setTelephone(telephone);
                users.get(i).setCcnumber(ccnumber);
                users.get(i).setCcexpirationdate(expiration);
                users.get(i).setPassword(password);      
                db.setUsers(users);
                return true;
            }
        }
        return false;
    }
       
}

