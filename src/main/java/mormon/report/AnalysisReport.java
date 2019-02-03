package mormon.report;

import java.util.Map;

/**
 * AnalysisReport
 *
 * An abstract representation of a report on some form of analysis that was performed. This is essentially an interface
 * between the extraction and annotation process, and the analysis process.
 */
public interface AnalysisReport {

    /**
     * Converts this report to a set of JSON strings indicating various pieces of information about this report, with
     * the key being the name of the JSON report type (such as general information about the report) and the value
     * being the actual data of that type in JSON, prepared to be written to a file.
     *
     * @return -
     */
    public Map<String, String> toJsonStrings();
}
