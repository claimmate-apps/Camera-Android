package com.commonsware.cwac.camera.demo.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JKSOL on 3/14/2018.
 */

public class InteriorRes {

    public List<Ceiling> ceilings = new ArrayList<>();

    public static class Ceiling {
        public int title;
        public int sign;
        public String info;
        public boolean isManually;

        public Ceiling(int title, int sign, String info) {
            this.title = title;
            this.sign = sign;
            this.info = info;
            this.isManually = isManually;
        }
    }

}
