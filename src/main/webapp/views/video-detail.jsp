<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${video.title}</title>
</head>
<body>
    <a href="${pageContext.request.contextPath}/home">← Trang chủ</a> | 
    <a href="${pageContext.request.contextPath}/video">← Danh sách Video</a>
    <hr/>

    <h2>${video.title}</h2>
    <p>Mã Video: <b>${video.videoId}</b> | 
       Người đăng: <b>${video.user != null ? video.user.fullName : 'Admin'}</b> | 
       Lượt xem: <b>${video.views}</b></p>

    <div>
        <c:choose>
            <c:when test="${not empty video.videoFile && video.videoFile.contains('youtube.com/embed')}">
                <iframe width="720" height="405" src="${video.videoFile}" frameborder="0" allowfullscreen></iframe>
            </c:when>
            <c:otherwise>
                <c:url value="/image?fname=${video.videoFile}" var="vidSrc" />
                <video width="720" height="405" controls autoplay>
                    <source src="${video.videoFile != null && video.videoFile.startsWith('http') ? video.videoFile : vidSrc}" type="video/mp4">
                    Trình duyệt của bạn không hỗ trợ phát video.
                </video>
            </c:otherwise>
        </c:choose>
    </div>

    <h3>Mô tả:</h3>
    <p>${video.description}</p>
</body>
</html>