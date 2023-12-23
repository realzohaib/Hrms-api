//package com.erp.hrms.test;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.persistence.EntityManager;
//import javax.persistence.NoResultException;
//import javax.persistence.Query;
//import javax.persistence.TypedQuery;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import com.erp.hrms.api.dao.PersonalInfoDAO;
//import com.erp.hrms.entity.PersonalInfo;
//import com.erp.hrms.exception.PersonalInfoNotFoundException;
//
//public class PersonalInforRepoTest {
//
//	@Mock
//	private EntityManager entityManager;
//
//	@InjectMocks
//	private PersonalInfoDAO personalInfoRepo;
//
//	@BeforeEach
//	public void setup() {
//		MockitoAnnotations.openMocks(this);
//	}
//
//	@Test
//	public void testExistsByEmail() {
//		String email = "test@example.com";
//
//		TypedQuery<Long> query = mock(TypedQuery.class);
//		when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(query);
//		when(query.setParameter(eq("email"), eq(email))).thenReturn(query);
//		when(query.getSingleResult()).thenReturn(1L);
//
//		boolean result = personalInfoRepo.existsByEmail(email);
//		assertTrue(result);
//	}
//
//	@Test
//	public void testSavePersonalInfo() {
//		PersonalInfo personalInfo = new PersonalInfo();
//
//		personalInfoRepo.savePersonalInfo(personalInfo);
//
//		verify(entityManager, times(1)).persist(personalInfo);
//	}
//
//	@Test
//	public void testFindAllPersonalInfo() {
//		List<PersonalInfo> expectedList = new ArrayList<>();
//		PersonalInfo info1 = new PersonalInfo();
//		PersonalInfo info2 = new PersonalInfo();
//		expectedList.add(info1);
//		expectedList.add(info2);
//		TypedQuery<PersonalInfo> query = mock(TypedQuery.class);
//		when(entityManager.createQuery(anyString(), eq(PersonalInfo.class))).thenReturn(query);
//		when(query.getResultList()).thenReturn(expectedList);
//		List<PersonalInfo> resultList = personalInfoRepo.findAllPersonalInfo();
//		assertEquals(expectedList.size(), resultList.size());
//		assertEquals(expectedList.get(0), resultList.get(0));
//		assertEquals(expectedList.get(1), resultList.get(1));
//	}
//
//	@Test
//	public void testGetPersonalInfoByEmail_Found() {
//		String email = "existing@example.com";
//
//		Query queryMock = mock(Query.class);
//		when(entityManager.createQuery(anyString())).thenReturn(queryMock);
//		when(queryMock.setParameter(anyString(), any())).thenReturn(queryMock);
//		PersonalInfo personalInfoMock = mock(PersonalInfo.class);
//		when(queryMock.getSingleResult()).thenReturn(personalInfoMock);
//		PersonalInfo result = personalInfoRepo.getPersonalInfoByEmail(email);
//		assertEquals(personalInfoMock, result);
//	}
//
//	
//	 @Test
//	    public void testGetPersonalInfoByEmail_NotFound() {
//	        String email = "nonexistent@example.com";
//	        TypedQuery<PersonalInfo> typedQuery = mock(TypedQuery.class);
//	        when(entityManager.createQuery(anyString(), eq(PersonalInfo.class))).thenReturn(typedQuery);
//	        when(typedQuery.setParameter(anyString(), any())).thenReturn(typedQuery);
//	        when(typedQuery.getSingleResult()).thenThrow(new NoResultException());
//	        assertThrows(PersonalInfoNotFoundException.class, () -> personalInfoRepo.getPersonalInfoByEmail(email));
//	    }
//
//	@Test
//	public void testGetPersonalInfoByEmployeeId_Found() {
//		Long employeeId = 67890L;
//		Query queryMock = mock(Query.class);
//		when(entityManager.createQuery(anyString())).thenReturn(queryMock);
//		when(queryMock.setParameter(anyString(), any())).thenReturn(queryMock);
//		PersonalInfo personalInfoMock = mock(PersonalInfo.class);
//		when(queryMock.getSingleResult()).thenReturn(personalInfoMock);
//		PersonalInfo result = personalInfoRepo.getPersonalInfoByEmployeeId(employeeId);
//		assertEquals(personalInfoMock, result);
//	}
//
//	
//	@Test
//    public void testGetPersonalInfoByEmployeeId_NotFound() {
//		Long employeeId = 12345L;
//        TypedQuery<PersonalInfo> typedQuery = mock(TypedQuery.class);
//        when(entityManager.createQuery(anyString(), eq(PersonalInfo.class))).thenReturn(typedQuery);
//        when(typedQuery.setParameter(anyString(), any())).thenReturn(typedQuery);
//        when(typedQuery.getSingleResult()).thenThrow(new NoResultException());
//        assertThrows(PersonalInfoNotFoundException.class, () -> personalInfoRepo.getPersonalInfoByEmployeeId(employeeId));
//    }
//	
//	@Test
//	public void testGetPersonalInfoByEmailForUpdate_Found() {
//		String email = "existing@example.com";
//
//		Query queryMock = mock(Query.class);
//		when(entityManager.createQuery(anyString())).thenReturn(queryMock);
//		when(queryMock.setParameter(anyString(), any())).thenReturn(queryMock);
//		PersonalInfo personalInfoMock = mock(PersonalInfo.class);
//		when(queryMock.getSingleResult()).thenReturn(personalInfoMock);
//		PersonalInfo result = personalInfoRepo.getPersonalInfoByEmailForUpdate(email);
//		assertEquals(personalInfoMock, result);
//	}
//	
//	    @Test
//	    public void testGetPersonalInfoByEmailForUpdateNotFound() {
//	        String email = "nonexistent@example.com";
//	        TypedQuery<PersonalInfo> typedQuery = mock(TypedQuery.class);
//	        when(entityManager.createQuery(anyString(), eq(PersonalInfo.class))).thenReturn(typedQuery);
//	        when(typedQuery.setParameter(anyString(), any())).thenReturn(typedQuery);
//	        when(typedQuery.getSingleResult()).thenThrow(new NoResultException());
//	        assertThrows(PersonalInfoNotFoundException.class, () -> personalInfoRepo.getPersonalInfoByEmailForUpdate(email));
//	    }
//	
//	 @Test
//	    public void testDeletePersonalInfoByEmail() {
//	        String email = "test@example.com";
//	        PersonalInfo personalInfo = new PersonalInfo();
//	        personalInfo.setEmail(email);
//	        personalInfoRepo.deletePersonalInfoByEmail(email, personalInfo);
//	        verify(entityManager, times(1)).merge(personalInfo);
//	    }
//
//	@Test
//	public void testUpdatePersonalInfo() {
//		String email = "test@example.com";
//		PersonalInfo personalInfo = new PersonalInfo();
//
//		Query query = mock(Query.class);
//		when(entityManager.createQuery(anyString())).thenReturn(query);
//		when(query.setParameter(eq("email"), eq(email))).thenReturn(query);
//
//		personalInfoRepo.updatePersonalInfo(email, personalInfo);
//
//		verify(entityManager, times(1)).merge(personalInfo);
//		assertEquals(email, personalInfo.getEmail());
//	}
//}