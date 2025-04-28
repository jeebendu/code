import org.hibernate.query.NativeQuery;
import org.hibernate.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class AppointmentRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Page<Object[]> getAppointments(SearchForm searchForm, Pageable pageable) {
        Session session = entityManager.unwrap(Session.class);

        // Call the stored procedure
        NativeQuery query = session.createSQLQuery("EXEC GetAppointmentsWithCounts :status, :caseManagerId, :type, :value, :dateType, :fromDate, :toDate, :pageNumber, :pageSize");

        // Set parameters
        query.setParameter("status", searchForm.getStatus());
        query.setParameter("caseManagerId", searchForm.getCaseManagerId());
        query.setParameter("type", searchForm.getType());
        query.setParameter("value", searchForm.getValue());
        query.setParameter("dateType", searchForm.getDateType());
        query.setParameter("fromDate", searchForm.getFromDate());
        query.setParameter("toDate", searchForm.getToDate());
        query.setParameter("pageNumber", pageable.getPageNumber() + 1); // Spring Pageable is 0-based
        query.setParameter("pageSize", pageable.getPageSize());

        // Execute query and get results
        List<Object[]> results = query.getResultList();

        // Extract total count and filtered count
        int totalCount = results.isEmpty() ? 0 : (int) results.get(0)[0];
        int filteredCount = results.isEmpty() ? 0 : (int) results.get(0)[1];

        // Return as a Page object
        return new PageImpl<>(results, pageable, filteredCount);
    }
}
