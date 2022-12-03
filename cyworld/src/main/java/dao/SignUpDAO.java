package dao;

import java.util.HashMap;
import java.util.List;

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
	public SignUpVO selectOneIdCheck(String userID) {
		SignUpVO vo = sqlSession.selectOne("s.doubleCheck", userID);
		return vo;
	}
	
	// 가입전 가입자인지 체크
	public SignUpVO selectJoinCheck(SignUpVO vo) {
		SignUpVO svo = sqlSession.selectOne("s.selectJoinCheck", vo);
		return svo;
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
	
	/////////////// 도토리 구역 ///////////////
	
	//도토리 구매
	public int buyDotory(SignUpVO vo) {
		int res = sqlSession.update("s.dotory_buy", vo);
		return res;
	}
	
	/////////////// 프로필 구역 ///////////////
	
	// 프로필 조회
	public List<SignUpVO> selectListIdx(int idx) {
		List<SignUpVO> list = sqlSession.selectList("s.profile_list", idx);
		return list;
	}
	
	// 프로필 미니미 변경
	public int updateMinimi(SignUpVO vo) {
		int res = sqlSession.update("s.profile_minimi", vo);
		return res;
	}
	
	// 프로필 좌측 - 메인 사진 및 메인 소개글 수정
	public int updateMain(SignUpVO vo) {
		int res = sqlSession.update("s.profile_modify_main", vo);
		return res;
	}
	
	// 프로필 우측 (cyworld 가입자) - 메인 타이틀 및 비밀번호 수정
	public int updateUserData(SignUpVO vo) {
		int res = sqlSession.update("s.profile_modify_userdata", vo);
		return res;
	}
	
	// 프로필 우측 (소셜 가입자) - 비밀번호 없이 메인 타이틀만 수정
	public int updateSocialUserData(SignUpVO vo) {
		int res = sqlSession.update("s.profile_modify_social_userdata", vo);
		return res;
	}
}
