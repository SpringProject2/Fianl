package dao;

import java.util.HashMap;

import org.apache.ibatis.session.SqlSession;

import vo.SignUpVO;

public class SignUpDAO {
	// SqlSession
	SqlSession sqlSession;
	// SI방식
	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}
	
	// 플랫폼 + 이메일 확인 절차 (가입자와 비가입자 구분용)
	public SignUpVO selectOnePlatformEmail(SignUpVO vo) {
		SignUpVO joinVo = sqlSession.selectOne("s.join", vo);
		return joinVo;
	}
	
	// ID 중복 확인
	public SignUpVO selectOneDoubleCheck(String userID) {
		SignUpVO vo = sqlSession.selectOne("s.doubleCheck", userID);
		return vo;
	}
	
	// 가입 성공시 고객 정보 저장
	public int insertJoinSuccess(SignUpVO vo) {
		int res = sqlSession.insert("s.joinSuccess", vo);
		return res;
	}
	
	// ID 찾기
	public SignUpVO selectOneId(SignUpVO vo) {
		SignUpVO idVo = sqlSession.selectOne("s.findID", vo);
		return idVo;
	}
	
	// PW 찾기
	public SignUpVO selectOnePw(SignUpVO vo) {
		SignUpVO pwVo = sqlSession.selectOne("s.findPW", vo);
		return pwVo;
	}
	
	// 임시 PW 갱신
	public int updateNewPw(HashMap<String, String> m_key) {
		int res = sqlSession.update("s.newPw", m_key);
		return res;
	}
	
	// IDX기준 회원정보 가져오기
	public SignUpVO selectOneIdx(Object idx) {
		SignUpVO vo = sqlSession.selectOne("s.selectIdx", idx);
		return vo;
	}
}
