package lu.cnfpc.edujobapp.dto.external;

import java.util.List;

public class ArbeitNowResponse {
    private List<ArbeitNowJob> data;
    // Paging meta data can be added if needed, but strict requirements don't ask for it.

    public List<ArbeitNowJob> getData() {
        return data;
    }

    public void setData(List<ArbeitNowJob> data) {
        this.data = data;
    }
}
