package xyz.n7mn.dev.api.data.eq;

import java.util.Date;

public class Head {

    private String Title;
    private Date ReportDateTime;
    private Date TargetDateTime;
    private long EventID;
    private String InfoType;
    private String Serial;
    private String InfoKind;
    private String InfoKindVersion;
    private String Headline;

    public String getTitle() {
        return Title;
    }

    public Date getReportDateTime() {
        return ReportDateTime;
    }

    public Date getTargetDateTime() {
        return TargetDateTime;
    }

    public long getEventID() {
        return EventID;
    }

    public String getInfoType() {
        return InfoType;
    }

    public String getSerial() {
        return Serial;
    }

    public String getInfoKind() {
        return InfoKind;
    }

    public String getInfoKindVersion() {
        return InfoKindVersion;
    }

    public String getHeadline() {
        return Headline;
    }
}
