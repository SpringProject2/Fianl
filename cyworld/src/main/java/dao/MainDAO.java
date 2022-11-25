package dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import vo.MainVO;
import vo.SignUpVO;

public class MainDAO {

	SqlSession sqlSession;
	
	public MainDAO(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}
	
	//일촌평 전체목록 조회
	public List<MainVO> selectList(int idx){
		List<MainVO> list = sqlSession.selectList("m.ilchon_list", idx);
		return list;
	}
	
	// 이름으로 검색한 사용자 조회
	public List<SignUpVO> selectSearchName(String searchValue) {
		List<SignUpVO> list = sqlSession.selectList("m.main_search_name", searchValue);
		return list;
	}
	// 이름으로 검색한 사용자 조회
	public List<SignUpVO> selectSearchId(String searchValue) {
		List<SignUpVO> list = sqlSession.selectList("m.main_search_id", searchValue);
		return list;
	}
	// 이름으로 검색한 사용자 조회
	public List<SignUpVO> selectSearchEmail(String searchValue) {
		List<SignUpVO> list = sqlSession.selectList("m.main_search_email", searchValue);
		return list;
	}
	
	//새 글 추가
	public int insert(MainVO vo) {
		//mapper로 딱 한개의 객체만 넘겨줄 수 있다. 
		int res = sqlSession.insert("m.main_insert", vo);
		return res; 
	}
	
	// 일촌평 갯수 구하기
	public int selectCountNum() {
		int res = sqlSession.selectOne("m.countNum");
		return res;
	}
	
	// 가장 최근에 작성한 일촌평 구하기
	public int selectMaxNum() {
		int res = sqlSession.selectOne("m.maxNum");
		return res;
	}
}