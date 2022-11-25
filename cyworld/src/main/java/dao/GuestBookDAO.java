package dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import vo.GuestBookVO;


public class GuestBookDAO {
	
	SqlSession sqlSession;
	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}
	

	//방명록 전체 목록 조회
	public List<GuestBookVO> selectList(int idx){
		List<GuestBookVO> list = sqlSession.selectList("gb.guestbook_list", idx);
		return list;
	}
	
	// 작성된 글 갯수 구하기
	public int selectCountNum(int idx) {
		int res = sqlSession.selectOne("gb.countNum", idx);
		return res;
	}
		
	// 가장 최근에 작성한 일촌평 구하기
	public int selectMaxNum(int idx) {
		int res = sqlSession.selectOne("gb.maxNum", idx);
		return res;
	}
	
	//새 글 추가
	public int insert(GuestBookVO vo) {
		int res = sqlSession.insert("gb.guestbook_insert", vo);
		return res;
	}

	//글 삭제
	public int delete(GuestBookVO vo) {
		int res = sqlSession.delete("gb.guestbook_delete", vo);
		return res;
	}

	//수정을 위한 게시글 한 건 조회
	public GuestBookVO selectOne(GuestBookVO vo) {
		GuestBookVO updateVo = sqlSession.selectOne("gb.guestbook_one", vo);
		return updateVo;
	}

	//게시글 수정
	public int update(GuestBookVO vo) {
		int res = sqlSession.update("gb.guestbook_update", vo);
		return res;
	}
}