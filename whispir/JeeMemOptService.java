import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.log4j.Log4j2;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
@Transactional
public class JeeMemOptService {
    private final PropertiesConfig env;
    private final JeeMemOptDao jeeMemOptDao;

    public JeeMemOptService(PropertiesConfig env, JeeMemOptDao jeeMemOptDao) {
        this.env = env;
        this.jeeMemOptDao = jeeMemOptDao;
    }

    public List<JEEMember> getMember(String pInsuredNRICFIN, String pDOB) {
        if (isInvalidInput(pInsuredNRICFIN, pDOB)) {
            return null;
        }
        BigDecimal bdob = pDOB != null ? new BigDecimal(pDOB) : BigDecimal.ZERO;
        return env.isProduction() ? jeeMemOptDao.callMemberInfo(pInsuredNRICFIN, bdob) : findDummyMemberByNric(pInsuredNRICFIN);
    }

    public List<JEEMember> getMemberForMedix(String pInsuredNRICFIN, String pDOB) {
        if (isInvalidInput(pInsuredNRICFIN, pDOB)) {
            return null;
        }
        return env.isProduction() ? jeeMemOptDao.callMemberInfoMedix(pInsuredNRICFIN, pDOB) : findDummyMemberByNric(pInsuredNRICFIN);
    }

    public List<JEEMember> getMemberByNIRC(String insuredNRICFIN) {
        return env.isProduction() ? jeeMemOptDao.callMemberByNIRC(insuredNRICFIN) : findDummyMemberByNric(insuredNRICFIN);
    }

    public List<JEEMember> getMemberByNIRCAdminCareTeam(String insuredNRICFIN) {
        return env.isProduction() ? jeeMemOptDao.callMemberInfo(insuredNRICFIN, null) : findDummyMemberByNric(insuredNRICFIN);
    }

    private boolean isInvalidInput(String nric, String dob) {
        return nric == null || dob == null || nric.isEmpty() || dob.isEmpty() || nric.startsWith("Na") || dob.startsWith("Na");
    }

    private List<JEEMember> findDummyMemberByNric(String nric) {
        // Dummy implementation
        return new ArrayList<>();
    }
}