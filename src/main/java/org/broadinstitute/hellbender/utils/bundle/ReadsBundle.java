package org.broadinstitute.hellbender.utils.bundle;

import com.google.common.base.Charsets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.collections.map.HashedMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.broadinstitute.hellbender.engine.GATKPathSpecifier;
import org.broadinstitute.hellbender.exceptions.UserException;
import org.broadinstitute.hellbender.utils.Utils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Collections;

public class ReadsBundle {
    public static final String BUNDLE_EXTENSION = ".json";
    private static final Logger LOG = LogManager.getLogger(ReadsBundle.class);
    private static final String SCHEMA_VERSION = "0.1.0";
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static final String FILE_TYPE_KEY = "FILE_TYPE";
    private final BundleFile reads;
    private final BundleFile index;
    private final String schemaVersion;


    public ReadsBundle(final BundleFile reads, final BundleFile index) {
        this.schemaVersion = SCHEMA_VERSION;
        this.reads = reads;
        this.index = index;
    }

    public static ReadsBundle fromPath(GATKPathSpecifier path){
        try(final InputStreamReader inputStreamReader = new InputStreamReader(new BufferedInputStream(path.getInputStream()), Charsets.UTF_8)) {
            final ReadsBundle readsBundle = GSON.fromJson(inputStreamReader, ReadsBundle.class);
            checkSchema(readsBundle);
            return readsBundle;
        } catch (final IOException e) {
            throw new UserException.CouldNotReadInputFile("Failed to load reads bundle json from: " +path.getRawInputString(), e);
        }
    }

    private static void checkSchema(final ReadsBundle readsBundle) {
        if (!SCHEMA_VERSION.equals(readsBundle.schemaVersion)) {
            LOG.warn("Loaded a reads bundle with a non-matching schema number.  Expected:"+ SCHEMA_VERSION +" but found: " + readsBundle.schemaVersion);
        }
    }

    public static ReadsBundle fromJson(String json){
        final ReadsBundle readsBundle = GSON.fromJson(json, ReadsBundle.class);
        checkSchema(readsBundle);
        return readsBundle;
    }

    public String toJson(){
        return GSON.toJson(this);
    }

    public GATKPathSpecifier getReads() {
        return getPathSpecifierWithTypeTag(reads);
    }

    public GATKPathSpecifier getIndex() {
        return getPathSpecifierWithTypeTag(index);
    }

    private static GATKPathSpecifier getPathSpecifierWithTypeTag(final BundleFile index) {
        final GATKPathSpecifier path = new GATKPathSpecifier(index.getPath());
        path.setTagAttributes(Collections.singletonMap(FILE_TYPE_KEY, index.getFileType()));
        return path;
    }
}

