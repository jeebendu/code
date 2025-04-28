CREATE PROCEDURE GetAppointmentsWithCounts
    @status NVARCHAR(50) = NULL,
    @caseManagerId INT = NULL,
    @type NVARCHAR(50) = NULL,
    @value NVARCHAR(255) = NULL,
    @dateType NVARCHAR(10) = NULL,
    @fromDate DATETIME = NULL,
    @toDate DATETIME = NULL,
    @pageNumber INT = 1,
    @pageSize INT = 10
AS
BEGIN
    SET NOCOUNT ON;

    -- Adjust @toDate to the end of the day if it is not NULL
    IF @toDate IS NOT NULL
    BEGIN
        SET @toDate = DATEADD(SECOND, -1, DATEADD(DAY, 1, CAST(@toDate AS DATE)));
    END

    DECLARE @offset INT = (@pageNumber - 1) * @pageSize;

    -- Total Count (without filters)
    DECLARE @totalCount INT;
    SELECT @totalCount = COUNT(*)
    FROM Apptrequeststatusrecord;

    -- Filtered Count (with filters)
    DECLARE @filteredCount INT;
    SELECT @filteredCount = COUNT(*)
    FROM Apptrequeststatusrecord ARSR
    JOIN appointment_request appt ON ARSR.appointment_request_id = appt.id
    JOIN appointment_requeststatus apptStatus ON ARSR.appointment_requeststatus_id = apptStatus.id
    WHERE 1 = 1
        -- Apply filters (same as below for paginated data)
        AND (
            @status IS NULL OR @status = ''
            OR (
                @status = 'myown' AND (
                    (LOWER(apptStatus.name) = 'assigned' OR LOWER(apptStatus.name) = 'pending')
                    AND appt.casemanagerid = @caseManagerId
                )
            )
            OR (
                LOWER(@status) = 'pending' AND LOWER(apptStatus.name) = 'pending'
            )
            OR (
                LOWER(@status) = 'new' AND (
                    LOWER(apptStatus.name) = 'assigned' OR LOWER(apptStatus.name) = 'new'
                )
            )
            OR (
                LOWER(apptStatus.name) = LOWER(@status)
            )
        )
        AND (@caseManagerId IS NULL OR @caseManagerId = 0 OR appt.casemanagerid = @caseManagerId)
        AND (
            @type IS NULL OR @type = '' OR @value IS NULL OR @value = ''
            OR LOWER(appt.@type) LIKE '%' + LOWER(@value) + '%'
        )
        AND (
            @dateType IS NULL OR @dateType = ''
            OR (
                @dateType = '0' AND (
                    (@fromDate IS NULL OR appt.date_of_submission >= @fromDate)
                    AND (@toDate IS NULL OR appt.date_of_submission <= @toDate)
                )
            )
            OR (
                @dateType = '1' AND (
                    (@fromDate IS NULL OR appt.requestoptionldate >= @fromDate)
                    AND (@toDate IS NULL OR appt.requestoptionldate <= @toDate)
                )
            )
            OR (
                @dateType = '2' AND (
                    (@fromDate IS NULL OR appt.confirmedappointmentdate >= @fromDate)
                    AND (@toDate IS NULL OR appt.confirmedappointmentdate <= @toDate)
                )
            )
        );

    -- Paginated Data
    SELECT @totalCount AS totalCount, @filteredCount AS filteredCount, *
    FROM Apptrequeststatusrecord ARSR
    JOIN appointment_request appt ON ARSR.appointment_request_id = appt.id
    JOIN appointment_requeststatus apptStatus ON ARSR.appointment_requeststatus_id = apptStatus.id
    WHERE 1 = 1
        -- Apply filters
        AND (
            @status IS NULL OR @status = ''
            OR (
                @status = 'myown' AND (
                    (LOWER(apptStatus.name) = 'assigned' OR LOWER(apptStatus.name) = 'pending')
                    AND appt.casemanagerid = @caseManagerId
                )
            )
            OR (
                LOWER(@status) = 'pending' AND LOWER(apptStatus.name) = 'pending'
            )
            OR (
                LOWER(@status) = 'new' AND (
                    LOWER(apptStatus.name) = 'assigned' OR LOWER(apptStatus.name) = 'new'
                )
            )
            OR (
                LOWER(apptStatus.name) = LOWER(@status)
            )
        )
        AND (@caseManagerId IS NULL OR @caseManagerId = 0 OR appt.casemanagerid = @caseManagerId)
        AND (
            @type IS NULL OR @type = '' OR @value IS NULL OR @value = ''
            OR LOWER(appt.@type) LIKE '%' + LOWER(@value) + '%'
        )
        AND (
            @dateType IS NULL OR @dateType = ''
            OR (
                @dateType = '0' AND (
                    (@fromDate IS NULL OR appt.date_of_submission >= @fromDate)
                    AND (@toDate IS NULL OR appt.date_of_submission <= @toDate)
                )
            )
            OR (
                @dateType = '1' AND (
                    (@fromDate IS NULL OR appt.requestoptionldate >= @fromDate)
                    AND (@toDate IS NULL OR appt.requestoptionldate <= @toDate)
                )
            )
            OR (
                @dateType = '2' AND (
                    (@fromDate IS NULL OR appt.confirmedappointmentdate >= @fromDate)
                    AND (@toDate IS NULL OR appt.confirmedappointmentdate <= @toDate)
                )
            )
        )
    ORDER BY appt.id
    OFFSET @offset ROWS FETCH NEXT @pageSize ROWS ONLY;
END;
