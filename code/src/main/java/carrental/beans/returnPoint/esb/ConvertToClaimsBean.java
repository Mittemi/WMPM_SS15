package carrental.beans.returnPoint.esb;

import carrental.model.pickupPoint.Claim;
import carrental.model.pickupPoint.ClaimType;
import org.apache.camel.Converter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Michael on 09.06.2015.
 */
public class ConvertToClaimsBean {

    public List<Claim> convert(List<List<String>> csvInput) {

        return csvInput.stream().map(l -> parseClaim(l)).collect(Collectors.toList());
    }

    private Claim parseClaim(List<String> line) {
        Claim claim = new Claim();

        claim.setClaimType(ClaimType.valueOf(line.get(0)));
        claim.setDescription(line.get(1));

        return claim;
    }
}
