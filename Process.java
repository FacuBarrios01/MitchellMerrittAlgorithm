public class Process{
    private int privateId;
    private int publicId;

    // Initially private and public ids will be random and unique values,
    // we will take care of that in the main method of MitchellMerritt.java when creating the processes
    public Process(int id){
        this.privateId = id;
        this.publicId = id;
    }

    public void setPrivateId(int id){
        this.privateId = id;
    }

    public void setPublicId(int id){
        this.publicId = id;
    }

    public void getPublicId(){
        return this.publicId;
    }

    public void getPrivateId(){
        return this.privateId;
    }
}