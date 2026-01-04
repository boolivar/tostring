package org.bool.tostring.agent;

public class Options {

    private static final String FORCE = "--force";

    private String regex;

    private boolean force;

    public static Options parse(String args) {
        Options options = new Options();
        if (args != null && !args.isEmpty()) {
            for (String a : args.split(",")) {
                String arg = a.trim();
                if (FORCE.equals(arg)) {
                    options.setForce(true);
                } else {
                    options.setRegex(arg);
                }
            }
        }
        return options;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public boolean isForce() {
        return force;
    }

    public void setForce(boolean force) {
        this.force = force;
    }
}
