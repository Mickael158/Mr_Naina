package test;


import etu1904.framework.annotation.ActionMethod;
import etu1904.framework.type.ScopeType;
import etu1904.framework.ModelView;
import etu1904.framework.MethodAnnotation;

import test.Test;

@Scope( type = ScopeType.SINGLETON)
public class Singleton {
    private int test;

    @MethodAnnotation( url = "s.do")
    public ModelView save() {
        ModelView mv = new ModelView();

        Test t = new Test();
        t.setId(test);
        t.setNom("Singleton");

        mv.setView("/test.jsp");
        mv.addItem("obj", t);


        test++;

        return mv;
    }

    public int getTest() {
        return test;
    }

    public void setTest(int test) {
        this.test = test;
    }
    
}
