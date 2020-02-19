package com.commonsware.cwac.camera.demo.model;

import java.io.Serializable;

public class ClaimModel implements Serializable {
    private String id, name, shortName;
    private String user_id, claim_id, cause_of_loss, date_of_loss, mortgage, labor_min, company, stories, type_of_construction, rci, single_family, garages, exterior_siding, insured_person, inspection_date, op, depreciation, contents, salvage, subrogation;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getClaim_id() {
        return claim_id;
    }

    public void setClaim_id(String claim_id) {
        this.claim_id = claim_id;
    }

    public String getCause_of_loss() {
        return cause_of_loss;
    }

    public void setCause_of_loss(String cause_of_loss) {
        this.cause_of_loss = cause_of_loss;
    }

    public String getDate_of_loss() {
        return date_of_loss;
    }

    public void setDate_of_loss(String date_of_loss) {
        this.date_of_loss = date_of_loss;
    }

    public String getMortgage() {
        return mortgage;
    }

    public void setMortgage(String mortgage) {
        this.mortgage = mortgage;
    }

    public String getLabor_min() {
        return labor_min;
    }

    public void setLabor_min(String labor_min) {
        this.labor_min = labor_min;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getStories() {
        return stories;
    }

    public void setStories(String stories) {
        this.stories = stories;
    }

    public String getType_of_construction() {
        return type_of_construction;
    }

    public void setType_of_construction(String type_of_construction) {
        this.type_of_construction = type_of_construction;
    }

    public String getRci() {
        return rci;
    }

    public void setRci(String rci) {
        this.rci = rci;
    }

    public String getSingle_family() {
        return single_family;
    }

    public void setSingle_family(String single_family) {
        this.single_family = single_family;
    }

    public String getGarages() {
        return garages;
    }

    public void setGarages(String garages) {
        this.garages = garages;
    }

    public String getExterior_siding() {
        return exterior_siding;
    }

    public void setExterior_siding(String exterior_siding) {
        this.exterior_siding = exterior_siding;
    }

    public String getInsured_person() {
        return insured_person;
    }

    public void setInsured_person(String insured_person) {
        this.insured_person = insured_person;
    }

    public String getInspection_date() {
        return inspection_date;
    }

    public void setInspection_date(String inspection_date) {
        this.inspection_date = inspection_date;
    }

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public String getDepreciation() {
        return depreciation;
    }

    public void setDepreciation(String depreciation) {
        this.depreciation = depreciation;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getSalvage() {
        return salvage;
    }

    public void setSalvage(String salvage) {
        this.salvage = salvage;
    }

    public String getSubrogation() {
        return subrogation;
    }

    public void setSubrogation(String subrogation) {
        this.subrogation = subrogation;
    }
}
