<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
          integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <script src="https://code.jquery.com/jquery-3.4.1.slim.min.js"
            integrity="sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"
            integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo" crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"
            integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6" crossorigin="anonymous"></script>
    <title>Accident</title>
</head>
<body>
<div class="container">
    <div class="row">
        <ul class="nav">
            <li class="nav-item" style="font-weight: bold">
                <a class="nav-link" href="<c:url value="/create"/>">Добавить инцидент</a>
            </li>
        </ul>
        <ul class="nav">
            <li class="nav-item" style="font-weight: bold">
                <a class="nav-link" href="<c:url value="/logout"/>"><c:out value="${user.username}"/> | Выйти из системы</a>
            </li>
        </ul>
    </div>
    <div class="row">
        <div class="card" style="width: 100%">
            <div class="card-header" style="font-weight: bold; font-size: larger">
                Онлайн-страница для отображения и создания инцидентов на дороге
            </div>
            <div class="card-body">
                <table class="table table-striped, table-bordered">
                    <thead>
                    <tr>
                        <th scope="col">Номер</th>
                        <th scope="col">Название</th>
                        <th scope="col">Описание</th>
                        <th scope="col">Тип</th>
                        <th scope="col">Статьи</th>
                        <th scope="col">Адрес</th>
                        <th scope="col">Действия</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${accidents}" var="accident">
                        <tr>
                            <td>
                                <c:out value="${accident.id}"/>
                            </td>
                            <td>
                                <c:out value="${accident.name}"/>
                            </td>
                            <td>
                                <c:out value="${accident.text}"/>
                            </td>
                            <td>
                                <c:out value="${accident.type.name}"/>
                            </td>
                            <td>
                                <c:forEach items="${accident.rules}" var="rule">
                                    <c:out value="${rule.name}"/>
                                </c:forEach>
                            </td>
                            <td>
                                <c:out value="${accident.address}"/>
                            </td>
                            <td>
                                <a href="<c:url value="/update?id=${accident.id}"/>">Редактировать</a>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
</body>
</html>
