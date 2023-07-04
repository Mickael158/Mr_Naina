package test;

import etu1904.framework.ModelView;
import etu1904.framework.FileUpload;
import etu1904.framework.annotation.ActionMethod;
import etu1904.framework.annotation.Auth;


public class Test {

    private int id;
    private String nom;


    @ActionMethod( url = "save.do")
    public ModelView save() {
        ModelView mv = new ModelView();

        mv.setView("/test.jsp");
        mv.addItem("obj", this);

        return mv;
    }

    @Auth( profil = "admin,simple")
    @ActionMethod( url = "setNewTest.do", paramName = "identifiant,name")
    public ModelView newTest(int identifiant, String name) {
        ModelView mv = new ModelView();

        this.setId(identifiant);
        this.setNom(name);

        mv.setView("/test.jsp");
        mv.addItem("obj", this);

        return mv;
    }

    @ActionMethod( url = "upload.do", paramName = "fu")
    public ModelView upload(FileUpload fu) {
        ModelView mv = new ModelView();

        this.setId(0);
        this.setNom("Elyse");

        mv.setView("/test.jsp");
        mv.addItem("obj", this);

        return mv;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return this.nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}