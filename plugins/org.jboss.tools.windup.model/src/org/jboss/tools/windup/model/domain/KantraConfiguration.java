/*---------------------------------------------------------------------------------------------
 *  Copyright (c) Red Hat. All rights reserved.
 *--------------------------------------------------------------------------------------------*/
package org.jboss.tools.windup.model.domain;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class KantraConfiguration {

    private String name;
    private String id;
    private Map<String, Object> options = Maps.newHashMap();
//    private ConfigurationNode node;
    private AnalysisResultsSummary summary;
    private List<Ruleset> rulesets;


    public AnalysisResultsSummary getSummary() {
        return this.summary;
    }

    public void setSummary(AnalysisResultsSummary summary) {
        this.summary = summary;
    }
    public void setRulesets(List<Ruleset> rulesets) {
        this.rulesets = rulesets;
    }

    public Map<String, Object> getOptions() {
        return this.options;
    }
    public void addOption(String option, Object value) {
        this.options.put(option, value);
    }
    public List<Ruleset> getRulesets() {
        return this.rulesets;
    }

    public void addRuleset (Ruleset ruleset){
        this.rulesets.add(ruleset);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

//    public ConfigurationNode getNode() {
//        return this.node;
//    }
//
//    public void setNode(ConfigurationNode node) {
//        this.node = node;
//    }

    public String getReportLocation() {
        String output = (String)this.options.get("output");
        if (output == null || output.isEmpty()) return null;
        Path location = Paths.get(output + File.separator +"static-report","index.html");
        return location.toAbsolutePath().toString();
    }
    public String getRulesetResultLocation() {
        String output = (String)this.options.get("output");
        if (output == null || output.isEmpty()) return null;
        Path location = Paths.get(output,"output.yaml");
        return location.toAbsolutePath().toString();
    }
    public boolean skippedReports() {
        String skippedReports = (String) this.getOptions().get("skipReports");
        return skippedReports != null ? Boolean.valueOf(skippedReports) : false;
    }

    public static String generateUniqueId() {
        return UUID.randomUUID().toString();
    }

    public static class AnalysisResultsSummary {
        public String executedTimestamp;
        public String executionDuration;
        public String outputLocation;
        public String executable;
        public int hintCount;
        public int classificationCount;

        private List<Ruleset> rulesets;
        private ModelService modelService;

        public void setRulesets(List<Ruleset> rulesets) {
            this.rulesets = rulesets;
        }
        public List<Ruleset> getRulesets() {
            return this.rulesets;
        }

        public Set<String> completeIssues = Sets.newHashSet();
        public Set<String> deletedIssues = Sets.newHashSet();
        public Map<String, String> reports = Maps.newHashMap();

        public List<Incident> incidents = Lists.newArrayList();
        public List<Classification> classifications = Lists.newArrayList();

        public AnalysisResultsSummary(ModelService modelService) {
            this.modelService = modelService;
        }

        public ModelService getModelService() {
            return this.modelService;
        }

        public List<Issue> getIssues() {
            List<Issue> issues = Lists.newArrayList();
            issues.addAll(this.incidents);
            issues.addAll(this.classifications);
            return issues;
        }

    }

    public static class Ruleset {
        private String name;
        private String description;
        private List<String> unmatched = Lists.newArrayList();
        private List<String> tags = Lists.newArrayList();;
        private List<String> skipped = Lists.newArrayList();
        private Map<String, Violation> violations;
        private Map<String, String> errors;

        public Map<String, String> getErrors() {
            return errors;
        }

        public void setErrors(Map<String, String> errors) {
            this.errors = errors;
        }

        public Map<String, Violation> getViolations() {
            return violations;
        }

        public void setViolations(Map<String, Violation> violations) {
            this.violations = violations;
        }

        public String getDescription() {
            return this.description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public List<String> getSkipped() {
            return this.skipped;
        }

        public void setSkipped(List<String> skipped) {
            this.skipped = skipped;
        }

        public String getName() {
            return this.name;
        }

        public List<String> getUnmatched() {
            return this.unmatched;
        }

        public List<String> getTags() {
            return this.tags;
        }


        public void setName(String name) {
            this.name = name;
        }

        public void setUnmatched(List<String> unmatched) {
            this.unmatched = unmatched;
        }

        public void setTags(List<String> tags) {
            this.tags = tags;
        }

        public void addTags(String tag){
            this.tags.add(tag);
        }
        public void addViolation(String key,Violation violation){
            this.violations.put(key, violation);
        }

    }

    public static enum Category{
        POTENTIAL("potential"),
        OPTIONAL("optional"),
        MANDATORY("mandatory");
        String type;
        Category(String type){
            this.type = type;
        }

    }
    public static class Violation{
        private String description;
        private List<String> labels  = Lists.newArrayList();
        private List<Incident> incidents = Lists.newArrayList();
        private List<Link> links = Lists.newArrayList();
        private int effort;
        private String category ;

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public List<Link> getLinks() {
            return links;
        }

        public void setLinks(List<Link> links) {
            this.links = links;
        }



        public List<Incident> getIncidents() {
            return incidents;
        }

        public void setIncidents(List<Incident> incidents) {
            this.incidents = incidents;
        }

        public String getDescription() {
            return this.description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public List<String> getLabels() {
            return this.labels;
        }

        public void setLabels(List<String> labels) {
            this.labels = labels;
        }


        public int getEffort() {
            return this.effort;
        }


        public void setEffort(int effort) {
            this.effort = effort;
        }

        public void addLabel(String label){
            this.labels.add(label);
        }

        public void addIncidents(Incident incident){
            this.incidents.add(incident);
        }

    }

    public static class Incident extends Issue{
        private String uri;
        private String message;
        private String codeSnip;
        private int lineNumber;
        private  Map<String, String > variables;

        public Map<String, String> getVariables() {
            return variables;
        }

        public void setVariables(Map<String, String> variables) {
            this.variables = variables;
        }

        public void addVariables(String key,String value){
            this.variables.put(key, value);
        }
        public String getUri() {
            return this.uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }

        public String getMessage() {
            return this.message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getCodeSnip() {
            return this.codeSnip;
        }

        public void setCodeSnip(String codeSnip) {
            this.codeSnip = codeSnip;
        }

        public int getLineNumber() {
            return this.lineNumber;
        }

        public void setLineNumber(int lineNumber) {
            this.lineNumber = lineNumber;
        }


    }

    public static class Link{
        private String url;
        private String title;
        public String getUrl() {
            return url;
        }
        public void setUrl(String url) {
            this.url = url;
        }
        public String getTitle() {
            return title;
        }
        public void setTitle(String title) {
            this.title = title;
        }
    }

    public static class UniqueElement {
        public String id;
    }

    public static class Classification extends Issue {
        public String description;
    }
    public static class Issue extends UniqueElement {
        public String title;
        public String originalLineSource;
        public String file;
        public String severity;
        public String ruleId;
        public String effort;
        public ArrayList<Link> links = Lists.newArrayList();
        public String report;
        public String category;
        public KantraConfiguration configuration;
        public Object dom;
        public boolean complete;
        public boolean deleted;
    }


}
