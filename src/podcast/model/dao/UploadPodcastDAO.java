package podcast.model.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import podcast.model.idao.IUploadPodcastDAO;
import podcast.model.javabean.uploadPodcastBean;

@Repository("UploadPodcastDAO")
public class UploadPodcastDAO implements IUploadPodcastDAO {
	@Autowired
	@Qualifier("sessionFactory")
	private SessionFactory sessionFactory;

	public UploadPodcastDAO() {
	}

	public UploadPodcastDAO(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public uploadPodcastBean insert(uploadPodcastBean ubean) throws Exception {
		Session session = sessionFactory.getCurrentSession();

		session.save(ubean);
		return ubean;

	}

	@Override
	public uploadPodcastBean select(Integer podcastId) throws Exception {
		Session session = sessionFactory.getCurrentSession();
		return session.get(uploadPodcastBean.class, podcastId);
	}

	@Override
	public List<uploadPodcastBean> selectAll() throws Exception {
		Session session = sessionFactory.getCurrentSession();
		String hbl = "from uploadPodcastBean";

		Query<uploadPodcastBean> query = session.createQuery(hbl, uploadPodcastBean.class);

		List<uploadPodcastBean> lists = query.list();

		return lists;
	}
	
	@Override
	public List<String> fuzzySelectPodcastAllName(){
		Session session = sessionFactory.getCurrentSession();
		String hbl = "from uploadPodcastBean";
		Query<uploadPodcastBean> query = session.createQuery(hbl, uploadPodcastBean.class);
		
		List<String> podcastAllDataName = new ArrayList<String>();
    	for(uploadPodcastBean i:query.list()) {
    		podcastAllDataName.add(i.getTitle());	
    	}
		return podcastAllDataName;
		
	}

	@Override
	public List<uploadPodcastBean> selectAllFromMember(Integer memberId) throws Exception {
		Session session = sessionFactory.getCurrentSession();
		String hbl = "from uploadPodcastBean where memberId=:memberId";

		Query<uploadPodcastBean> query = session.createQuery(hbl, uploadPodcastBean.class);
		query.setParameter("memberId", memberId);

		List<uploadPodcastBean> upList = query.list();

		return upList;
	}

	@Override
	public uploadPodcastBean update(Integer podcastId, uploadPodcastBean ubean) throws Exception {
		Session session = sessionFactory.getCurrentSession();
		uploadPodcastBean oldbean = session.get(uploadPodcastBean.class, podcastId);
		
		if(oldbean != null) {
			oldbean.setOpenComment(ubean.getOpenComment());
			oldbean.setOpenPayment(ubean.getOpenPayment());
			oldbean.setTitle(ubean.getTitle());
			oldbean.setPodcastInfo(ubean.getPodcastInfo());
		}

		return oldbean;
	}

	@Override
	public boolean delete(Integer podcastId) throws Exception {
		Session session = sessionFactory.getCurrentSession();
		uploadPodcastBean ubean = select(podcastId);

		if (ubean != null) {
			session.delete(ubean);
			System.out.println("delete done");
			return true;
		}

		return false;
	}

	public List<uploadPodcastBean> queryProgramByMemberID(Integer memberId) {
		Session session = sessionFactory.getCurrentSession();
		String hql="from uploadPodcastBean up where up.memberId= :mid";
		
		List<uploadPodcastBean> resultlist = 
				(List<uploadPodcastBean>)session.createQuery(hql).setParameter("mid", memberId).getResultList();
		return resultlist;

	}
	
	
	//點擊時點級數加一
	public uploadPodcastBean addClickCount(Integer podcastID) {
		
		uploadPodcastBean ubean= sessionFactory.getCurrentSession().get(uploadPodcastBean.class,podcastID);
		
		ubean.setClickAmount(ubean.getClickAmount()+1);
		
		return ubean;
		
	}

}
