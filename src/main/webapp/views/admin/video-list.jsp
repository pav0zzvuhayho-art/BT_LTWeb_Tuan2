<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Quản lý Video (CRUD & Multipart Upload)</title>
</head>
<body>
    <a href="${pageContext.request.contextPath}/home">← Trang chủ</a> | 
    <a href="${pageContext.request.contextPath}/video">Xem trang /video (6sp/trang)</a>
    <hr/>

    <h2>${not empty video ? 'Cập nhật Video' : 'Thêm Video Mới'}</h2>
    <form action="${pageContext.request.contextPath}/admin/video/${not empty video ? 'update' : 'insert'}" 
          method="post" enctype="multipart/form-data">
        
        <label>Video ID:</label><br/>
        <input type="text" name="videoId" value="${video.videoId}" ${not empty video ? 'readonly' : 'required'} /><br/><br/>

        <label>Tiêu đề:</label><br/>
        <input type="text" name="title" value="${video.title}" required /><br/><br/>

        <label>Upload Ảnh đại diện:</label><br/>
        <input type="file" name="posterFile" accept="image/*" /><br/><br/>

        <label>Upload File Video (.mp4):</label><br/>
        <input type="file" name="videoFileUpload" accept="video/mp4,video/*" /><br/><br/>
        <label>Hoặc Link Video:</label><br/>
        <input type="text" name="videoUrl" value="${video.videoFile}" /><br/><br/>

        <label>Mô tả:</label><br/>
        <textarea name="description" rows="3" cols="40">${video.description}</textarea><br/><br/>

        <label>Trạng thái:</label>
        <input type="radio" name="active" value="1" ${empty video || video.active == 1 ? 'checked' : ''} /> Hoạt động
        <input type="radio" name="active" value="0" ${not empty video && video.active == 0 ? 'checked' : ''} /> Khóa
        <br/><br/>

        <button type="submit">${not empty video ? 'Update Video' : 'Insert Video'}</button>
        <c:if test="${not empty video}">
            <a href="${pageContext.request.contextPath}/admin/videos">Hủy</a>
        </c:if>
    </form>

    <hr/>
    <h2>Danh sách Videos</h2>
    <table border="1" width="100%">
        <tr>
            <th>ID</th>
            <th>Poster</th>
            <th>Tiêu đề</th>
            <th>Người tạo (User)</th>
            <th>Views</th>
            <th>Trạng thái</th>
            <th>Hành động</th>
        </tr>
        <c:forEach items="${videoList}" var="v">
            <tr>
                <td>${v.videoId}</td>
                <td>
                    <c:url value="/image?fname=${v.poster}" var="pUrl" />
                    <img src="${v.poster != null && v.poster.startsWith('http') ? v.poster : pUrl}" width="100" height="70" />
                </td>
                <td><a href="${pageContext.request.contextPath}/video/detail?id=${v.videoId}">${v.title}</a></td>
                <td>${v.user != null ? v.user.userName : 'Admin'}</td>
                <td>${v.views}</td>
                <td>${v.active == 1 ? 'Hoạt động' : 'Khóa'}</td>
                <td>
                    <a href="${pageContext.request.contextPath}/admin/video/edit?id=${v.videoId}">Sửa</a> |
                    <a href="${pageContext.request.contextPath}/admin/video/delete?id=${v.videoId}" onclick="return confirm('Xóa video này?');">Xóa</a>
                </td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>