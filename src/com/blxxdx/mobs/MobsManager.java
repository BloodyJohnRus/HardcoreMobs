package com.blxxdx.mobs;

import com.blxxdx.mobs.instance.MobInstance;

import java.util.HashMap;
import java.util.Map;

public class MobsManager {
    public Map<String, MobInstance> instances;
    public MobsManager(){
        instances = new HashMap<>();
    }

}
