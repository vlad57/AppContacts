package eu.epitech.vladwp.appcontacts;


public class Model {

    private int id;
    private String name;
    private String number;
    private String email;
    private byte[] image;

    //Le putain de constructeur de ces morts
    public Model(){
    }

    public Model (int Id, String Name, String Number, String Email, byte[] Image){
        this.id = Id;
        this.name = Name;
        this.number = Number;
        this.email = Email;
        this.image = Image;
    }

    //Méthodes de récupérations
    public int getId(){return this.id;}
    public String getName(){return this.name;}
    public String getNumber(){return this.number;}
    public String getEmail(){return this.email;}
    public byte[] getImage(){return this.image;}

    //Méthodes de setage
    public void setId(int Id){this.id = Id;}
    public void setName(String Name){this.name = Name;}
    public void setNumber(String Number){this.number = Number;}
    public void setEmail(String Email){this.email = Email;}
    public void setImage(byte[] Image){this.image = Image;}
}
