package org.broadinstitute.hellbender.utils.bundle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ReadsBundle {
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    final BundleFile reads;
    final BundleFile index;



    public ReadsBundle(final BundleFile reads, final BundleFile index) {
        this.reads = reads;
        this.index = index;
    }

    public static ReadsBundle fromJson(String json){
        return GSON.fromJson(json, ReadsBundle.class);
    }

    public String toJson(){
        return GSON.toJson(this);
    }

    public static class ReadsAndIndex {
        private final BundleFile reads;
        private final BundleFile index;

        public ReadsAndIndex(final BundleFile reads, final BundleFile index) {
            this.reads = reads;
            this.index = index;
        }
    }
}

