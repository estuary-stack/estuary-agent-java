package com.github.dinuta.estuary.agent.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class YamlConfig {

    @JsonProperty("env")
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private Map<String, String> env = new LinkedHashMap<>();

    @JsonProperty("before_script")
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private List<String> beforeScript = new ArrayList<>();

    @JsonProperty("script")
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private List<String> script = new ArrayList<>();

    @JsonProperty("after_script")
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private List<String> afterScript = new ArrayList<>();

    public List<String> getAfterScript() {
        return afterScript;
    }

    public void setAfterScript(List<String> afterScript) {
        this.afterScript = afterScript;
    }

    public List<String> getBeforeScript() {
        return beforeScript;
    }

    public void setBeforeScript(List<String> beforeScript) {
        this.beforeScript = beforeScript;
    }

    public List<String> getScript() {
        return script;
    }

    public void setScript(List<String> script) {
        this.script = script;
    }

    public Map<String, String> getEnv() {
        return env;
    }

    public void setEnv(Map<String, String> env) {
        this.env = env;
    }
}
