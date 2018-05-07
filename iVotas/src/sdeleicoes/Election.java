package sdeleicoes;
 
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;
 
public class Election implements Serializable{
   private String category, title, description,department;
   private Date start,end;
   private int totalVotesStud,totalVotesFunc,totalVotesDoc;
   private CopyOnWriteArrayList<CandidatesList> candidatesStud,candidatesFunc,candidatesDoc;
   private  CopyOnWriteArrayList<Vote> whiteVotesStud,whiteVotesFunc,whiteVotesDoc,nullVotesStud,nullVotesDoc,nullVotesFunc;
   private HashMap<String,Vote> voters; //Nome do eleitor e se já votou ou não
   private boolean state;
   private CopyOnWriteArrayList<Table> tables;
   private int id;
   
   //Falta adicionar mesas no construtor
   //Construtor para conselho geral, eleição não tem departamento

    /**
     *
     * @param category - Categoria da Eleicao. Pode ser "nucleoestudantes" ou "conselhogeral"
     * @param title - titulo da eleicao
     * @param description
     * @param start - data de inicio da eleicao
     * @param end - data do fim da eleicao
     * @param id
     */
    public Election(String category, String title, String description, Date start, Date end,int id) {
        this.category = category;
        this.title = title;
        this.description = description;
        this.start = start;
        this.end = end;
        this.totalVotesStud = 0;
        this.totalVotesDoc = 0;
        this.totalVotesFunc = 0;
        this.candidatesStud = new CopyOnWriteArrayList<>();
        this.candidatesFunc = new CopyOnWriteArrayList<>();
        this.candidatesDoc = new CopyOnWriteArrayList<>();
        this.whiteVotesStud = new CopyOnWriteArrayList<>();
        this.whiteVotesDoc = new CopyOnWriteArrayList<>();
        this.whiteVotesFunc = new CopyOnWriteArrayList<>();
        this.nullVotesStud = new CopyOnWriteArrayList<>();
        this.nullVotesFunc = new CopyOnWriteArrayList<>();
        this.nullVotesDoc = new CopyOnWriteArrayList<>();
        this.voters = new HashMap<>();
        this.tables = new CopyOnWriteArrayList();
        this.state = true;
        this.id = id;
    }
    //Construtor para nucleo de Estudantes, eleição tem departamento

    /**
     *
     * @param category
     * @param department - departamento da eleição em caso de ser Nucleo de Estudantes
     * @param title
     * @param description
     * @param start
     * @param end
     * @param id
     */
    public Election(String category,String department, String title, String description, Date start, Date end,int id) {
        this.department = department;
        this.category = category;
        this.title = title;
        this.description = description;
        this.start = start;
        this.end = end;
        this.totalVotesStud = 0;
        this.totalVotesDoc = 0;
        this.totalVotesFunc = 0;
        this.candidatesStud = new CopyOnWriteArrayList<>();
        this.candidatesFunc = new CopyOnWriteArrayList<>();
        this.candidatesDoc = new CopyOnWriteArrayList<>();
        this.whiteVotesStud = new CopyOnWriteArrayList<>();
        this.whiteVotesDoc = new CopyOnWriteArrayList<>();
        this.whiteVotesFunc = new CopyOnWriteArrayList<>();
        this.nullVotesStud = new CopyOnWriteArrayList<>();
        this.nullVotesFunc = new CopyOnWriteArrayList<>();
        this.nullVotesDoc = new CopyOnWriteArrayList<>();
        this.tables = new CopyOnWriteArrayList<>();
        this.voters = new HashMap<>();
        this.state = true;
        this.id = id;
    }

    /**
     *
     * @param name - Nome do user
     * @return String com detalhes do voto se este já tiver votado
     */
    public String getInfoVoter(String name){
        Vote vote;
        if(this.voters.containsKey(name)){
            if((vote = this.voters.get(name))!=null){
                return String.format("Name: %s\nElection: %s\nVote Desk: %s\nDate: %s\n",name,this.title,vote.getDepartment(),vote.getDate().toString());
            }
            return "The user didn't vote yet.";
        }
        return "The user can't vote in this election.";
    }
    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public void setCandidatesStud(CopyOnWriteArrayList<CandidatesList> candidatesStud) {
        this.candidatesStud = candidatesStud;
    }
    public void setCandidatesFunc(CopyOnWriteArrayList<CandidatesList> candidatesFunc) {
        this.candidatesFunc = candidatesFunc;
    }

    public void setCandidatesDoc(CopyOnWriteArrayList<CandidatesList> candidatesDoc) {
        this.candidatesDoc = candidatesDoc;
    }
    
    /**
     *
     * @param users - Array com todos os users criados
     *  Vai inserir todos os users para serem votantes em caso de ser para o conselho geral
     *  Vai inserir todos os users estudantes do mesmo departamento em caso de ser para o nucleo de estudantes
     * @return true se bem sucedido
     */
    public boolean addVoters(CopyOnWriteArrayList<User> users){
        if(this.category.equalsIgnoreCase("conselhogeral")){
            for (int i = 0; i < users.size(); i++) {
                voters.put(users.get(i).getName().replaceAll(" ",""),null);
            }
            return true;
        }
        else if(this.category.equalsIgnoreCase("nucleoestudantes")){
            for (int i = 0; i < users.size(); i++) {
                if(users.get(i).getDepartment().equalsIgnoreCase(department))
                    voters.put(users.get(i).getName().replaceAll(" ",""),null);
            }
            return true;
        }
        return false;
            
    }

    /**
     *
     * @param name - Nome do utilizador
     * @param vote - Voto do utilizador
     * Procura o votante e insere o voto
     * @return true se bem sucedido
     */
    public boolean updateVoters(String name,Vote vote){
        if(this.voters.containsKey(name)){
            this.voters.replace(name, vote);
            return true;
        }
        return false;
    }

    public HashMap<String, Vote> getVoters() {
        return voters;
    }

    public int getTotalVotesStud() {
        return totalVotesStud;
    }

    public void addToTotalVotesStud() {
        this.totalVotesStud++;
    }

    public int getTotalVotesFunc() {
        return totalVotesFunc;
    }

    public void addToTotalVotesFunc() {
        this.totalVotesFunc++;
    }

    public int getTotalVotesDoc() {
        return totalVotesDoc;
    }

    public void addToTotalVotesDoc() {
        this.totalVotesDoc++;
    }

    public CopyOnWriteArrayList<Vote> getWhiteVotesStud() {
        return whiteVotesStud;
    }

    public void addWhiteVoteStud(Vote whiteVoteStud) {
        this.whiteVotesStud.add(whiteVoteStud);
    }

    public CopyOnWriteArrayList<Vote> getWhiteVotesFunc() {
        return whiteVotesFunc;
    }

    public void addWhiteVoteFunc(Vote whiteVoteFunc) {
        this.whiteVotesFunc.add(whiteVoteFunc);
    }

    public CopyOnWriteArrayList<Vote> getWhiteVotesDoc() {
        return whiteVotesDoc;
    }

    public void addWhiteVoteDoc(Vote whiteVoteDoc) {
        this.whiteVotesDoc.add(whiteVoteDoc);
    }

    public CopyOnWriteArrayList<Vote> getNullVotesStud() {
        return nullVotesStud;
    }

    public void addNullVoteStud(Vote nullVoteStud) {
        this.nullVotesStud.add(nullVoteStud);
    }

    public CopyOnWriteArrayList<Vote> getNullVotesDoc() {
        return nullVotesDoc;
    }

    public void addNullVoteDoc(Vote nullVoteDoc) {
        this.nullVotesDoc.add(nullVoteDoc);
    }

    public CopyOnWriteArrayList<Vote> getNullVotesFunc() {
        return nullVotesFunc;
    }

    public void addNullVoteFunc(Vote nullVoteFunc) {
        this.nullVotesFunc.add(nullVoteFunc);
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public CopyOnWriteArrayList<CandidatesList> getTotalCandidatesStud() {
        return candidatesStud;
    }

    public void addCandidatesStud(CandidatesList candidatesStud) {
        this.candidatesStud.add(candidatesStud);
    }

    public CopyOnWriteArrayList<CandidatesList> getTotalCandidatesFunc() {
        return candidatesFunc;
    }

    public void addCandidatesFunc(CandidatesList candidatesFunc) {
        this.candidatesFunc.add(candidatesFunc);
    }

    public CopyOnWriteArrayList<CandidatesList> getTotalCandidatesDoc() {
        return candidatesDoc;
    }

    public void addCandidatesDoc(CandidatesList candidatesDoc) {
        this.candidatesDoc.add(candidatesDoc);
    }
    
 
    
    public String getCategory() {
        return category;
    }
 
    public void setCategory(String category) {
        this.category = category;
    }
 
    public String getTitle() {
        return title;
    }
 
    public void setTitle(String title) {
        this.title = title;
    }
 
    public String getDescription() {
        return description;
    }
 
    public void setDescription(String description) {
        this.description = description;
    }
 
    
 
    public Date getEnd() {
        return end;
    }
 
    public void setEnd(Date end) {
        this.end = end;
    }
 
 public Date getStart() {
        return start;
    }
 
    public void setStart(Date start) {
        this.start = start;
    }

    public CopyOnWriteArrayList<Table> getTables() {
        return tables;
    }

    public void setTables(CopyOnWriteArrayList<Table> tables) {
        this.tables = tables;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @param table - Mesa de Voto
     *  Procura mesas de voto com o mesmo departamento nesta eleicao e atualiza-las
     */
    public void editTable(Table table){
        for (int i = 0; i < tables.size(); i++) {
            if(tables.get(i).getDepartment().equalsIgnoreCase(table.getDepartment()))
                tables.set(i, table);
        }
    }

    /**
     *
     * @param user
     *  Adiciona o votante para a lista de votantes
     */
    public void addVoter(User user){
        if(!voters.containsKey(user.getName())){
            voters.put(user.getName(),null);
        }
    }
   
}