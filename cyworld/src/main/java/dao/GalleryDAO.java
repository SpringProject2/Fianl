package dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import vo.GalleryVO;
import vo.GalleryCommentVO;
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
	
	// 글 삭제
	public int delete(GalleryVO vo) {
		int res = sqlSession.delete("g.gallery_delete", vo);
		return res;
	}
	
	// 글 삭제 후 삭제한 게시물 번호보다 큰 번호들을 조회
	public List<GalleryVO> selectListDelete(HashMap<String, Integer> map) {
		List<GalleryVO> list = sqlSession.selectList("g.gallery_list_delete", map);
		return list;
	}
	
	// 글 삭제 후 삭제한 게시물 번호보다 큰 번호들을 1씩 감소시켜서 갱신
	public int updateRefMinus(GalleryVO vo) {
		int res = sqlSession.update("g.gallery_update_ref_minus", vo);
		return res;
	}
	
	// 수정을 위한 게시글 한 건 조회
	public GalleryVO selectOneRef( GalleryVO vo ) {
		GalleryVO refVo = sqlSession.selectOne("g.gallery_one", vo);
		return refVo;
	}
	
	// 게시글 수정
	public int update( GalleryVO vo ) {
		int res = sqlSession.update("g.gallery_update", vo);
		return res;
	}
	
	///////////////////댓글 구역/////////////////////
	
	// 댓글 조회
	public List<GalleryCommentVO> selectCommentList(int idx){
		List<GalleryCommentVO> list = sqlSession.selectList("gc.comment_list", idx);
			return list;
		}

	// 새 댓글 추가
	public int insertComment(GalleryCommentVO vo) {
		int res = sqlSession.insert("gc.comment_insert", vo);
		return res; 
	}

	// 댓글 갯수 구하기
	public int selectCommentNum(GalleryCommentVO vo) {
		int res = sqlSession.selectOne("gc.commentNum", vo);
		return res;
	}
	
	// 게시글 삭제시 해당 댓글 내역도 모두 삭제
	public int deleteCommentAll(GalleryCommentVO vo) {
		int res = sqlSession.delete("gc.deleteCommentAll", vo);
		return res;
	}
	
	// 댓글 삭제
	public int deleteGComment(GalleryCommentVO vo) {
		int res = sqlSession.update("gc.deleteComment", vo);
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