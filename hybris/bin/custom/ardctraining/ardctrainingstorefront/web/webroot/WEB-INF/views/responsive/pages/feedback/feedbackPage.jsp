<%--
  Created by IntelliJ IDEA.
  User: ardc
  Date: 07/10/21
  Time: 12:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template" %>

<template:page pageTitle="Feedback">
    <c:choose>
        <c:when test="${not empty newFeedback}">
            <div id="js-cookie-notification" class="alert alert-success alert-dismissable cookie-alert--top">
                <button class="js-cookie-notification-accept close" aria-hidden="true" data-dismiss="alert" type="button">
                    ×</button>
                Feedback sent, thanks for your comments</div>
        </c:when>
        <c:when test="${not empty faildFeedback}">
            <div id="js-cookie-notification" class="alert alert-danger alert-dismissable cookie-alert--top">
                <button class="js-cookie-notification-accept close" aria-hidden="true" data-dismiss="alert" type="button">
                    ×</button>
                All fields must be filled</div>
        </c:when>
        <c:otherwise></c:otherwise>
    </c:choose>

    <div class="bg-info" style="margin-right: 10rem; margin-left: 10rem; margin-bottom: 5rem; border-radius: 5% !important; padding-bottom: 2rem;">
        <h1 class="bg-primary" style="padding: 1rem;" >Customer Feedback</h1>
        <h4 style="padding: 1rem;">Have you faced any issue?, feel free to  contact us, we well get back to you as son as we can!</h4>
        <%--@elvariable id="ardctrainingFeedbackForm" type="com.ardctraining.storefront.feedbackform.ArdctrainingFeedbackForm"--%>
        <c:url var="feedback" value="/feedback"/>
        <form:form id="ardctrainingFeedbackForm" action="${feedback}" method="post" modelAttribute="ardctrainingFeedbackForm" style="padding: 1rem;">
            <form:input cssClass="form-control" name="subject" id="subject" maxlength="50" placeholder="subject" path="subject"/>
            <br>
            <form:textarea cssClass="form-control" name="message" id="message" maxlength="500" placeholder="message" path="message"/>
            <br>
            <button class="btn btn-primary" type="submit" id="submit" style="border-radius: 10% !important;">Send ✉</button>
            <a href="" role="button" id="cancel">Cancel</a>
        </form:form>
    </div>
    <c:choose>
        <c:when test="${not empty feedbacks}">
            <table>
                <thead>
                <tr>
                    <th scope="col" style="background: #0a6ed1 !important">Submitted Date</th>
                    <th scope="col" style="background: #0a6ed1 !important">Subject</th>
                    <th scope="col" style="background: #0a6ed1 !important">Message</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${feedbacks}" var="feedback" varStatus="loop">
                    <tr>
                        <th scope="row" style="background: #008888 !important">${feedback.submittedDate}</th>
                        <th scope="row" style="background: #008888 !important">${feedback.subject}</th>
                        <th style="background: #008888 !important">${feedback.message}</th>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:when>
        <c:otherwise>
            <h3>You don't have any feedbacks yet</h3>
        </c:otherwise>
    </c:choose>

</template:page>
