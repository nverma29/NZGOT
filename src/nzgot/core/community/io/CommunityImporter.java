package nzgot.core.community.io;

import nzgot.core.community.Community;
import nzgot.core.community.OTU;
import nzgot.core.community.OTUs;
import nzgot.core.community.Reference;
import nzgot.core.community.util.NameParser;
import nzgot.core.community.util.NameSpace;
import nzgot.core.logger.Logger;
import nzgot.core.uc.UCParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.TreeSet;

/**
 * Community Matrix Importer
 * Not attempt to store reads as Sequence, but as String
 * both OTU and reference mapping file are uc format.
 * @author Walter Xie
 */
public class CommunityImporter extends OTUsImporter {

    public static void importOTUs (File otusFile, OTUs otus) throws IOException, IllegalArgumentException {

        BufferedReader reader = getReader(otusFile, "OTUs from");

        OTU otu = null;
        String line = reader.readLine();
        while (line != null) {
            if (line.startsWith(">")) {
//                line.replaceAll("size=", "");
                // the current label only contains otu name
                String otuName = line.substring(1);
                otu = new OTU(otuName);

                otus.addUniqueElement(otu);

            } else {
                // TODO add ref sequence
            }

            line = reader.readLine();
        }

        reader.close();
    }

    // default to create OTUs from mapping file
    public static void importOTUsAndMappingFromUCFile(File otuMappingUCFile, OTUs otus) throws IOException, IllegalArgumentException {
        importOTUsAndMappingFromUCFile(otuMappingUCFile, otus, true, null);
    }

    public static void importOTUsAndMappingFromUCFile(File otuMappingUCFile, OTUs otus, boolean canCreateOTU) throws IOException, IllegalArgumentException {
        importOTUsAndMappingFromUCFile(otuMappingUCFile, otus, canCreateOTU, null);
    }

    /**
     * Ideally otuMappingUCFile should have all OTUs,
     * so that the validation assumed to be done before this method
     * 1st set sampleType, default to BY_PLOT
     * 2nd load reads into each OTU, and parse label to get sample array
     * 3rd set sample array, and calculate Alpha diversity for each OTU
     * @param otuMappingUCFile
     * @param community
     * @throws java.io.IOException
     * @throws IllegalArgumentException
     */
    public static void importOTUMappingFromUCFile(File otuMappingUCFile, Community community, boolean canCreateOTU) throws IOException, IllegalArgumentException {
        TreeSet<String> samples = new TreeSet<>();

        // 1st, set sampleType, default to BY_PLOT
        community.setSampleType(NameSpace.BY_PLOT);
        Logger.getLogger().info("\nSet sample type: " + community.getSampleType());

        // 2nd, parse label to get sample
        importOTUsAndMappingFromUCFile(otuMappingUCFile, community, canCreateOTU, samples);

        // 3rd, set diversities and samples
        community.setSamplesAndDiversities(samples);
    }

    /**
     *
     * @param otuMappingUCFile
     * @param otus
     * @param canCreateOTU     if use otuMappingUCFile to create OTUs
     * @param samples          used only for community to store samples
     * @throws IOException
     * @throws IllegalArgumentException
     */
    public static void importOTUsAndMappingFromUCFile(File otuMappingUCFile, OTUs otus, boolean canCreateOTU, TreeSet<String> samples) throws IOException, IllegalArgumentException {
        NameParser nameParser = NameParser.getInstance();

        BufferedReader reader = getReader(otuMappingUCFile, "OTUs and OTU mapping from");

        String line = reader.readLine();
        while (line != null) {
            // 2 columns: 1st -> read id, 2nd -> otu name
            String[] fields = line.split(NameParser.COLUMN_SEPARATOR, -1);

            if (fields.length < 2) throw new IllegalArgumentException("Error: invalid mapping in the line: " + line);

            if (fields[UCParser.Record_Type_COLUMN_ID].contentEquals(UCParser.HIT)) {
                OTU otu;
                String otuName = fields[UCParser.Target_Sequence_COLUMN_ID];
                if (otus.containsOTU(otuName)) {
                    otu = (OTU) otus.getUniqueElement(otuName);

                    if (otu == null) {
                        throw new IllegalArgumentException("Error: find an invalid OTU " + fields[UCParser.Target_Sequence_COLUMN_ID] +
                                ", from the mapping file which does not exist in OTUs file !");
                    } else {
                        otu.addUniqueElement(fields[UCParser.Query_Sequence_COLUMN_ID]);

                        if (samples != null) {
                            // if by plot, then add plot to TreeSet, otherwise add subplot
                            String sampleType = ((Community) otus).getSampleType();
                            String sampleLocation = nameParser.getSampleBy(sampleType, fields[UCParser.Query_Sequence_COLUMN_ID]);
                            samples.add(sampleLocation);
                        }
                    }
                } else if (canCreateOTU) {
                    otu = new OTU(otuName);
                    otu.addUniqueElement(fields[UCParser.Query_Sequence_COLUMN_ID]);
                    otus.addUniqueElement(otu);
                }

            }

            line = reader.readLine();
        }

        reader.close();

    }

    public static void importRefSeqMappingFromUCFile(File refSeqMappingUCFile, OTUs otus) throws IOException, IllegalArgumentException {

        BufferedReader reader = getReader(refSeqMappingUCFile, "reference sequence mapping (to OTU) from");

        String line = reader.readLine();
        while (line != null) {
            // 3 columns: 1st -> identity %, 2nd -> otu name, 3rd -> reference sequence id
            String[] fields = line.split(NameParser.COLUMN_SEPARATOR, -1);

            if (fields.length < 3) throw new IllegalArgumentException("Error: invalid mapping in the line: " + line);

            if (fields[UCParser.Record_Type_COLUMN_ID].contentEquals(UCParser.HIT)) {
                OTU otu = (OTU) otus.getUniqueElement(fields[UCParser.Query_Sequence_COLUMN_ID]);
                if (otu == null) {
                    throw new IllegalArgumentException("Error: find an invalid OTU " + fields[UCParser.Query_Sequence_COLUMN_ID] +
                            ", from the mapping file which does not exist in OTUs file !");
                } else {
                    Reference<OTU, String> refSeq = new Reference<>(otu, fields[UCParser.Target_Sequence_COLUMN_ID]);
                    otu.setReference(refSeq);
                }
            }

            line = reader.readLine();
        }

        reader.close();
    }

}
