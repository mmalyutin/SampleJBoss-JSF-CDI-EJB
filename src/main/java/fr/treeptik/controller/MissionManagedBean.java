package fr.treeptik.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.inject.Inject;

import fr.treeptik.exception.ServiceException;
import fr.treeptik.model.Employe;
import fr.treeptik.model.Mission;
import fr.treeptik.model.TypeMission;
import fr.treeptik.service.MissionService;

@ManagedBean(name = "missionMB")
@SessionScoped
public class MissionManagedBean {

	@Inject
	private FacesContext facesContext;

	@Inject
	private MissionService missionService;

	private Mission mission;

	private ListDataModel<Mission> missions = new ListDataModel<>();

	private List<SelectItem> selectType = new ArrayList<>();

	@PostConstruct
	public void init() {

		setMission(new Mission());
		mission.setEmploye(new Employe());
	}

	// Permet d'initialiser la liste qui sera utiliser dans les datatables de primefaces
	public String initListMission() throws Exception {
		missions = new ListDataModel<Mission>();
		missions.setWrappedData(missionService.findAll());
		return "/mission/list";
	}

	public String initMission() throws Exception {
		init();
		return "/mission/create";
	}

	public String register() throws Exception {
		try {
			missionService.register(getMission());
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO, "Registered!",
					"Registration successful");
			facesContext.addMessage(null, m);
			init();
		} catch (Exception e) {
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getLocalizedMessage(),
					"Registration unsuccessful");
			facesContext.addMessage(null, m);
		}
		return initListMission();
	}

	public String remove() throws Exception {
		try {
			mission = missions.getRowData();
			missionService.removeById(mission.getId());
		} catch (Exception e) {
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getLocalizedMessage(),
					"Remove unsuccessful");
			facesContext.addMessage(null, m);
		}

		return initListMission();
	}

	public String modify() throws ServiceException {

		mission = missions.getRowData();
		System.out.println("Id mission " + mission.getId());
		missionService.findById(mission.getId());
		System.out.println("Nom mission " + mission.getNom());

		return "create";
	}

	public ListDataModel<Mission> findAll() throws Exception {
		// Pour primeface sortBy pour avoir une liste fixe

		// missions = new ListDataModel<>();
		//
		// try {
		// missions.setWrappedData(missionService.findAll());
		// } catch (Exception e) {
		// FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getLocalizedMessage(),
		// "FindAll unsuccessful");
		// facesContext.addMessage(null, m);
		// }

		return missions;
	}

	public Mission getMission() {
		return mission;
	}

	public void setMission(Mission mission) {
		this.mission = mission;
	}

	public ListDataModel<Mission> getMissions() {
		return missions;
	}

	public void setMissions(ListDataModel<Mission> missions) {
		this.missions = missions;
	}

	public List<SelectItem> getSelectType() {
		List<TypeMission> allType = Arrays.asList(TypeMission.values());
		selectType.clear();
		for (TypeMission typeMission : allType) {
			selectType.add(new SelectItem(typeMission.toString(), typeMission.toString()));
		}

		return selectType;
	}

	public void setSelectType(List<SelectItem> selectType) {
		this.selectType = selectType;
	}

}
