/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the 
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,  
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.treeptik.test;

import static org.junit.Assert.assertNotNull;

import java.util.logging.Logger;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import fr.treeptik.model.Employe;
import fr.treeptik.service.EmployeService;

@RunWith(Arquillian.class)
public class EmployeRegistrationTest {
	@Deployment
	public static Archive<?> createTestArchive() {
		WebArchive webArchive = ShrinkWrap.create(WebArchive.class, "test.war")
				.addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
				// Deploy our test datasource
				.addAsWebInfResource("test-ds.xml");

		webArchive.addPackages(true, "fr.treeptik");

		return webArchive;
	}

	@Inject
	EmployeService memberRegistration;

	@Inject
	Logger log;

	@Test
	public void testRegister() throws Exception {
		Employe newMember = new Employe();
		newMember.setNom("Jane Doe");
		newMember.setLogin("jane@mailinator.com");
		newMember = memberRegistration.register(newMember);

		log.info("MemberRegistration : " + memberRegistration);

		assertNotNull(newMember.getId());
		log.info(newMember.getLogin() + " was persisted with id " + newMember.getId());
	}

}
