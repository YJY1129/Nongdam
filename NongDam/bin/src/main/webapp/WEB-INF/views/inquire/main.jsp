<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set var="contextPath" value="${pageContext.request.contextPath }" />

<html lang="ko" data-bs-theme="light">
<head>
    <!-- UTF-8 사용-->
    <meta charset="UTF-8">
    <!-- 기본 화면 보기 설정 -->
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
	
    <!-- bootstrap 5.3.2 -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.8/dist/umd/popper.min.js" integrity="sha384-I7E8VVD/ismYTF4hNIPjVp/Zjvgyol6VFvRkX/vR+Vc4jQkC+hVqc2pM8ODewa9r" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.min.js" integrity="sha384-BBtl+eGJRgqQAUMxJ7pMwbEyER4l1g+O15P+16Ep7Q9Q+zqX6gSbd85u4mG4QzX+" crossorigin="anonymous"></script>

    <!--제이쿼리-->
    <script src="https://code.jquery.com/jquery-3.7.1.min.js" integrity="sha256-/JqT3SQfawRcv/BIHPThkBvs0OEvtFFmqPF/lYI/Cxo=" crossorigin="anonymous"></script>
    
    <!-- ckEditor 5 -->
    <script src="https://cdn.ckeditor.com/ckeditor5/40.1.0/classic/ckeditor.js"></script>

    <!-- fontAwesome -->
    <script src="https://kit.fontawesome.com/f34a67d667.js" crossorigin="anonymous"></script>
    
    <!-- 스타일시트 -->
    <link rel="stylesheet" href="${contextPath }/resources/common/css/style.css">
    <!-- 기본js -->
    <script type="text/javascript" src="${contextPath }/resources/common/js/common.js"></script>
    
    <meta name="농담" content="안녕하세요, 농업 정보 커뮤니티 농담입니다."/>
    
    <!-- 파비콘 -->
    <link rel="icon" type="image/x-icon" href="${contextPath }/resources/image/common/favicon.ico"/>
    <link rel="shortcut icon" type="image/x-icon" href="${contextPath }/resources/image/common/favicon.ico"/>
    
    <title>농담 | 농업 정보 커뮤니티</title>
    
</head>
<body>

	<jsp:include page="../common/header.jsp"/>
	<jsp:include page="../common/banner.jsp"/>
	
	
	
    <!-- 문의하기 -->
    <div class="container mt-5 mb-5">
        <div class="mt-5 mb-5">
            <div class="mb-4 d-flex flex-wrap justify-content-between">
                <h4>
                    이메일 문의
                </h4>
                <div>
                    <button type="button" class="btn btn-outline-secondary"><i class="fa-solid fa-xmark"></i> 문의 취소</button>
                    <button type="button" class="btn btn-secondary"><i class="fa-solid fa-paper-plane"></i> 문의하기</button>
                </div>
            </div>
            <hr>
            <div class="mb-3 row">
                <label for="user_pw2" class="col-sm-3 col-md-2 col-form-label">분류<span class="text-danger">*</span></label>
                <div class="col-sm-5 col-md-4 col-lg-3">
                    <select class="form-select">
                        <option>결제</option>
                        <option>계정</option>
                        <option>공동구매 신청</option>
                        <option>기타</option>
                    </select>
                </div>
            </div>
            <div class="mb-3 row">
                <label for="user_pw2" class="col-sm-3 col-md-2 col-form-label">제목<span class="text-danger">*</span></label>
                <div class="col-sm-9 col-md-10">
                    <input type="text" class="form-control" id="user_pw2" placeholder="제목을 입력해주세요.">
                    <!-- 아래 span태그를 가리려면 span class에 d-none을 추가하면 됩니다 -->
                    <!-- 편한 자리로 옮기거나 추가해서 사용하세요-->
                    <span class="text-danger">{에러메세지 또는 설명 텍스트}</span>
                </div>
            </div>
            <div class="mb-3 row">
                <label for="user_pw2" class="col-sm-3 col-md-2 col-form-label">내용<span class="text-danger">*</span></label>
                <div class="col-sm-9 col-md-10">
                    <textarea class="form-control" placeholder="문의 내용을 작성해주세요." style="min-height: 300px"></textarea>
                </div>
            </div>
            <div class="mb-3 row">
                <label for="user_pw2" class="col-sm-3 col-md-2 col-form-label">이메일 주소<span class="text-danger">*</span></label>
                <div class="col-sm-9 col-md-10">
                    <input type="email" class="form-control" id="user_pw2" placeholder="답장을 받으실 이메일 주소를 입력해주세요.">
                </div>
            </div>
            <div class="mb-3 row">
                <label for="formFile" class="col-sm-3 col-md-2 col-form-label">첨부 파일</label>
                <div class="col-sm-9 col-md-10">
                    <input class="form-control" type="file" id="formFile">
                </div>
            </div>
            
            <!-- 버튼 -->
            <div class="mt-5 mb-5 d-flex flex-wrap justify-content-center align-items-end">
            
                <div class="text-end">
                    <a class="btn btn-outline-secondary" href="#"><i class="fa-solid fa-xmark"></i> 문의 취소</a>
                    <button type="submit" class="btn btn-secondary"><i class="fa-solid fa-paper-plane"></i> 문의하기</button>
                </div>
            </div>
        </div>
    </div>
	
	<jsp:include page="../common/footer.jsp"/>
</body>
</html>
