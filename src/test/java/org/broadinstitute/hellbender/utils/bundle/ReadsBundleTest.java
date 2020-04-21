package org.broadinstitute.hellbender.utils.bundle;

import com.google.gson.Gson;
import ngs.Read;
import org.broadinstitute.hellbender.testutils.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class ReadsBundleTest extends BaseTest {

    @Test
    public void testSerialize() {
        ReadsBundle readsBundle = new ReadsBundle(new BundleFile("a file", "bam"), new BundleFile("an index", "bai"));
        Gson gson = new Gson();
        String s = gson.toJson(readsBundle);
        Assert.assertEquals(s, "{\"reads\":{\"path\":\"a file\",\"fileType\":\"bam\"},\"index\":{\"path\":\"an index\",\"fileType\":\"bai\"}}");

        String json = "{\"reads\":{\"path\":\"a file\",\"fileType\":\"bam\"},\"index\":{\"path\":\"an index\",\"fileType\":\"bai\"}}";
        ReadsBundle readsBundle1 = gson.fromJson(json, ReadsBundle.class);
        Assert.assertEquals(readsBundle1.reads.getPath(), "b file");
    }
}