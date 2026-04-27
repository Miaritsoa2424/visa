<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${empty pageTitle ? 'Visa' : pageTitle}</title>

    <link rel="stylesheet" href="/css/navbar.css">
    <c:if test="${pageStyle == 'index'}">
        <link rel="stylesheet" href="/css/index.css">
    </c:if>
    <c:if test="${pageStyle == 'demandes'}">
        <link rel="stylesheet" href="/css/demandes.css">
    </c:if>
    <c:if test="${pageStyle == 'demande-form'}">
        <link rel="stylesheet" href="/css/demande-form.css">
    </c:if>
    <c:if test="${pageStyle == 'demande-confirmation'}">
        <link rel="stylesheet" href="/css/demande-confirmation.css">
    </c:if>
</head>
<body>
<aside class="app-sidebar" aria-label="Navigation principale">
    <div class="sidebar-header">
        <a class="sidebar-brand" href="/home">
            <span class="brand-badge">&#9992;</span>
            <span class="sidebar-label">VisaTrack Madagascar</span>
        </a>
        <button id="sidebarToggle" class="sidebar-toggle" type="button" title="Reduire ou etendre le menu" aria-label="Reduire ou etendre le menu" aria-expanded="true">&#10094;&#10094;</button>
    </div>

    <nav class="sidebar-nav">
        <a class="sidebar-link" href="/home"><span class="sidebar-icon">&#8962;</span> <span class="sidebar-label">Accueil</span></a>

        <details class="sidebar-dropdown" open>
            <summary class="sidebar-summary"><span class="sidebar-icon">&#128221;</span> <span class="sidebar-label">Demandes</span> <span class="chevron">&#9662;</span></summary>
            <ul class="sidebar-submenu">
                <li><a href="/demandes"><span class="sidebar-icon">&#128203;</span> <span class="sidebar-label">Liste (/demandes)</span></a></li>
                <li><a href="/home"><span class="sidebar-icon">&#10133;</span> <span class="sidebar-label">Creation (/home)</span></a></li>
            </ul>
        </details>
    </nav>
</aside>

<main class="app-main">
    <c:if test="${not empty contentPage}">
        <jsp:include page="/WEB-INF/jsp/${contentPage}.jsp" />
    </c:if>

    <c:if test="${empty contentPage}">
        <div style="padding: 24px;">Aucun contenu a afficher.</div>
    </c:if>
</main>

<script>
    (function () {
        var toggleButton = document.getElementById("sidebarToggle");
        if (!toggleButton) {
            return;
        }

        var storageKey = "visa.sidebar.collapsed";

        function applyCollapsedState(isCollapsed) {
            document.body.classList.toggle("sidebar-collapsed", isCollapsed);
            toggleButton.setAttribute("aria-expanded", String(!isCollapsed));
            toggleButton.innerHTML = isCollapsed ? "&#9776;" : "&#10094;&#10094;";
        }

        var storedState = localStorage.getItem(storageKey) === "1";
        applyCollapsedState(storedState);

        toggleButton.addEventListener("click", function () {
            var shouldCollapse = !document.body.classList.contains("sidebar-collapsed");
            applyCollapsedState(shouldCollapse);
            localStorage.setItem(storageKey, shouldCollapse ? "1" : "0");
        });
    })();
</script>
</body>
</html>
