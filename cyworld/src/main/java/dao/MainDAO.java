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
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/////////////// 일촌평 구역 ///////////////
	
	// 일촌평 전체목록 조회
	public List<MainVO> selectList(int idx){
		List<MainVO> list = sqlSession.selectList("m.ilchon_list", idx);
		return list;
	}
	
	// 일촌평 작성
	public int ilchonWrite(MainVO vo) {
		int res = sqlSession.insert("m.ilchon_write", vo);
		return res; 
	}
	
	// idx에 해당하는 일촌평 갯수 구하기
	public int selectCountNum(int idx) {
		int res = sqlSession.selectOne("m.countNum", idx);
		return res;
	}
	
	/////////////// 검색 구역 ///////////////
	
	// 이름으로 검색한 사용자 조회
	public List<SignUpVO> selectSearchName(String searchValue) {
		List<SignUpVO> list = sqlSession.selectList("m.main_search_name", searchValue);
		return list;
	}
	// ID로 검색한 사용자 조회
	public List<SignUpVO> selectSearchId(String searchValue) {
		List<SignUpVO> list = sqlSession.selectList("m.main_search_id", searchValue);
		return list;
	} 
}