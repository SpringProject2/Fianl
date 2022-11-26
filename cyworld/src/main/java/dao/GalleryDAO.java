package dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import vo.GalleryVO;
import vo.GalleryLikeVO;

public class GalleryDAO {
	
	SqlSession sqlSession; //Mapper접근
	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}
	
	// 사진첩 전체목록 조회
	public List<GalleryVO> selectList(int idx){
		List<GalleryVO> list = sqlSession.selectList("g.gallery_list", idx);
		return list;
	}
	
	// 새 글 추가
	public int insert(GalleryVO vo) {
		//맵퍼로 딱 한개의 객체만 넘겨줄 수 있다.
		int res = sqlSession.insert("g.gallery_insert", vo);
		return res;
	}
	
	// 작성된 글 갯수 구하기
	public int selectCountNum(int idx) {
		int res = sqlSession.selectOne("g.countNum", idx);
		return res;
	}
	
	// 가장 최근에 작성한 일촌평 구하기
	public int selectMaxNum(int idx) {
		int res = sqlSession.selectOne("g.maxNum", idx);
		return res;
	}
	
	// 글 삭제
	public int delete( HashMap<String, Integer> galleryKey ) {
		int res = sqlSession.delete("g.gallery_delete", galleryKey);
		return res;
	}
	
	// 수정을 위한 게시글 한 건 조회
	public GalleryVO selectOne( HashMap<String, Integer> galleryKey ) {
		GalleryVO vo = sqlSession.selectOne("g.gallery_one", galleryKey);
		return vo;
	}
	
	// 게시글 수정
	public int update( GalleryVO vo ) {
		int res = sqlSession.update("g.gallery_update", vo);
		return res;
	}
	///////////////////좋아요 구역/////////////////////
	// 좋아요를 이미 눌렀는지 확인하기 위한 작업
	public GalleryLikeVO selectOneLike(GalleryLikeVO vo) {
		GalleryLikeVO likeVo = sqlSession.selectOne("gl.selectLike", vo);
		return likeVo;
	}
	
	// 게시물 좋아요 추가
	public int insertLike(GalleryLikeVO vo) {
		int res = sqlSession.insert("gl.addLike", vo);
		return res;
	}
	
	// 게시물 좋아요 취소
	public int deleteLike(GalleryLikeVO vo) {
		int res = sqlSession.delete("gl.cancleLike", vo);
		return res;
	}
	
	// 게시물 좋아요 갯수 구하기 -->
	public int selectLikeCountNum(GalleryLikeVO vo) {
		int res = sqlSession.selectOne("gl.likeCountNum", vo);
		return res;
	}
	
	// --> 구해낸 게시물 좋아요 갯수를 보여주기위해 컬럼에 작성하기
	public int updateLikeNum(GalleryVO vo) {
		int res = sqlSession.update("gl.likeNum", vo);
		return res;
	}
	
	// 게시글 삭제시 해당 좋아요 내역도 모두 삭제
	public int deleteLikeAll(GalleryLikeVO vo) {
		int res = sqlSession.delete("gl.deleteLikeAll", vo);
		return res;
	}
}