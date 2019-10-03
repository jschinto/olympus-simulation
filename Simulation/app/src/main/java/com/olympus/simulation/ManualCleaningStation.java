package com.olympus.simulation;

public class ManualCleaningStation {
    Scope currentScope;
    LeakTester_Type currentLeakTester;

    public ManualCleaningStation(LeakTester_Type type){
        this.currentScope = null;
        this.currentLeakTester = type;
    }

    public Scope getCurrentScope() {
        return currentScope;
    }

    public void setCurrentScope(Scope currentScope) {
        this.currentScope = currentScope;
    }

    public LeakTester_Type getCurrentLeakTester() {
        return currentLeakTester;
    }

    public void setCurrentLeakTester(LeakTester_Type currentLeakTester) {
        this.currentLeakTester = currentLeakTester;
    }

    public boolean validate(){
        if(this.currentLeakTester != null){
            return true;
        }

        return false;
    }
}
