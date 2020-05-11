package org.broadinstitute.hellbender.cmdline.argumentcollections;

import htsjdk.samtools.SAMUtils;
import htsjdk.samtools.SamFiles;
import org.broadinstitute.hellbender.engine.GATKPathSpecifier;
import org.broadinstitute.hellbender.utils.Utils;
import org.broadinstitute.hellbender.utils.io.IOUtils;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ReadIndexPair {
    private final GATKPathSpecifier reads;
    private final GATKPathSpecifier index;

    public ReadIndexPair(final GATKPathSpecifier reads, final GATKPathSpecifier index) {
        this.reads = Utils.nonNull(reads);
        this.index = index;
    }

    private ReadIndexPair(final Path reads, final Path index){
        this(IOUtils.toGATKPathSpecifier(reads), IOUtils.toGATKPathSpecifier(index));
    }

    public GATKPathSpecifier getReads() {
        return reads;
    }

    public GATKPathSpecifier getIndex() {
        return index;
    }

    public static List<ReadIndexPair> fromPathLists(List<Path> reads, List<Path> indexes){
        Utils.nonNull(reads);
        Utils.nonNull(indexes);
        return fromLists(Utils.map(reads, IOUtils::toGATKPathSpecifier), Utils.map(indexes, IOUtils::toGATKPathSpecifier));
    }

    public static List<ReadIndexPair> fromLists(List<GATKPathSpecifier> reads, List<GATKPathSpecifier> indexes){
        Utils.nonNull(reads);
        Utils.nonNull(indexes);
        Utils.validate(reads.size() == indexes.size(),
                String.format("reads (%d) and indexes (%d) must be the same length", reads.size(), indexes.size()));

        return IntStream.range(0, reads.size())
                .mapToObj(i -> new ReadIndexPair(reads.get(i), indexes.get(i)))
                .collect(Collectors.toList());
    }

    public static ReadIndexPair guessPairFromReads(GATKPathSpecifier reads){
        final Path index = SamFiles.findIndex(reads.toPath());
        return new ReadIndexPair(reads, IOUtils.toGATKPathSpecifier(index));
    }
}
