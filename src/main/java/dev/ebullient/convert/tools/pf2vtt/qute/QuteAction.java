package dev.ebullient.convert.tools.pf2vtt.qute;

import dev.ebullient.convert.tools.Tags;


import dev.ebullient.convert.tools.pf2vtt.Pf2VttSources;
import io.quarkus.qute.TemplateData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class QuteAction extends Pf2VttQuteBase {

    /** Trigger for this action */
    public final String trigger;
    /** Collection of traits (decorated links) */
    public final Collection<String> traits;
    /** Situational requirements for performing this action */
    public final String requirements;
    /** Prerequisite trait or characteristic for performing this action */
    public final String prerequisites;
    /** How often this action can be used */
    public final String frequency;
    /** The cost of using this action */
    public final String cost;
    /** Activity/Activation cost (as {@link QuteDataActivity QuteDataActivity}) */
    public final QuteDataActivity activity;

    public QuteAction(Pf2VttSources sources, List<String> text, Tags tags,
                      String cost, String trigger, List<String> aliases, Collection<String> traits,
                      String prerequisites, String requirements, String frequency,
                      QuteDataActivity activity, QuteAction actionType) {
        super(sources, text, tags);
        this.trigger = trigger;
        this.traits = traits;

        this.prerequisites = prerequisites;
        this.requirements = requirements;
        this.cost = cost;
        this.frequency = frequency;

        this.activity = activity;
    }

}
