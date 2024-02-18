package dev.ebullient.convert.tools.pf2vtt.qute;

import io.quarkus.qute.TemplateData;
import io.quarkus.runtime.annotations.RegisterForReflection;

@TemplateData
@RegisterForReflection
public class QutePublication {

    public String license;
    public String remaster;
    // Change this to be an enum which maps to the url of the book
    public String title;

    public QutePublication(String license, String remaster, String title) {
        this.license = license;
        this.remaster = remaster;
        this.title = title;
    }

    @Override
    public String toString() {
        return "QutePublication{" +
            "license='" + license + '\'' +
            ", remaster='" + remaster + '\'' +
            ", title='" + title + '\'' +
            '}';
    }
}
