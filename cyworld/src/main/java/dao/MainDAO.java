package dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import vo.IlchonVO;
import vo.MainVO;
import vo.SignUpVO;
import vo.ViewsVO;

public class MainDAO {
	// SqlSession
	SqlSession sqlSession;
	// CI방식
	public MainDAO(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/////////////// 조회수 구역 ///////////////
	
	// 현재 날짜로 로그인한 유저가 해당 미니홈피에 접속한 기록 조회
	public ViewsVO selectViewsToday(HashMap<String, Object> map) {
		ViewsVO vo = sqlSession.selectOne("m.selectViewsToday", map);
		return vo;
	}
	
	// 검색결과가 없을경우 첫방문 저장
	public int insertViewsToday(HashMap<String, Object> map) {
		int res = sqlSession.insert("m.insertViewsToday", map);
		return res;
	}
	
	// 검색결과가 있을경우 날짜비교후 날짜가 다르면 해당 날짜에 현재 날짜 갱신
	public int updateViewsToday(HashMap<String, Object> map) {
		int res = sqlSession.update("m.updateViewsToday", map);
		return res;
	}
	
	// 일일 조회수 증가
	public int updateTodayCount(SignUpVO vo) {
		int res = sqlSession.update("m.updateTodayCount", vo);
		return res;
	}
	
	// 날짜 바뀌어 총합 조회수 증가 및 일일 조회수 초기화
	public int updateTotalCount(SignUpVO vo) {
		int res = sqlSession.update("m.updateTotalCount", vo);
		return res;
	}
	
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
	
	/////////////// 일촌 구역 ///////////////
	
	// 일촌 이중 조회
	public int selectIlchonSearch(HashMap<String, Object> ilchonMap) {
		int res = sqlSession.selectOne("m.selectIlchonSearch", ilchonMap);
		return res;
	}
	
	// 일촌 등록
	public int insertIlchon(IlchonVO vo) {
		int res = sqlSession.insert("m.insertIlchon", vo);
		return res;
	}
	
	// 일촌 2차 조회
	public IlchonVO selectIlchon(IlchonVO vo) {
		IlchonVO ivo = sqlSession.selectOne("m.selectIlchon", vo);
		return ivo;
	}
	
	// 일촌 갱신
	public int updateIlchon(IlchonVO vo) {
		int res = sqlSession.update("m.updateIlchon", vo);
		return res;
	}
	
	// 일촌 해제
	public int deleteIlchon(IlchonVO vo) {
		int res = sqlSession.delete("m.deleteIlchon", vo);
		return res;
	}
	
	// 일촌수 조회
	public int selectIlchonNum(IlchonVO vo) {
		int res = sqlSession.selectOne("m.selectIlchonNum", vo);
		return res;
	}
	
	// 조회된 일촌수 갱신
	public int updateIlchonNum(SignUpVO vo) {
		int res = sqlSession.update("m.updateIlchonNum", vo);
		return res;
	}
	
	// 로그인한 유저와 해당 미니홈피 유저의 일촌관계
	public IlchonVO selectIlchonUp(IlchonVO vo) {
		IlchonVO ivo = sqlSession.selectOne("m.selectIlchonUp", vo);
		return ivo;
	}
	
	// 일촌 리스트 조회
	public List<IlchonVO> selectIlchonList(IlchonVO vo) {
		List<IlchonVO> list = sqlSession.selectList("m.selectIlchonList", vo);
		return list;
	}
}