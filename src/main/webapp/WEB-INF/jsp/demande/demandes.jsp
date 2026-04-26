<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="container">
    <h1>Liste des demandes</h1>
    <p class="subtitle">Consultez rapidement toutes les demandes saisies dans le systeme.</p>
    <a class="btn btn-primary" href="/home">Retour a l'accueil</a>

    <c:choose>
        <c:when test="${not empty demandes}">
            <table>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Date demande</th>
                    <th>Demandeur</th>
                    <th>Numéro passeport</th>
                    <th>Type visa</th>
                    <th>Type demande</th>
                    <th>Action</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${demandes}" var="demande">
                    <tr class="js-demande-row" data-href="/demande/fiche?id=${demande.id}" style="cursor: pointer;">
                        <td>${demande.id}</td>
                        <td>${demande.dateDemande}</td>
                        <td>${demande.passeport.personne.prenom} ${demande.passeport.personne.nom}</td>
                        <td>${demande.passeport.numero}</td>
                        <td>${demande.typeVisa.libelle}</td>
                        <td>${demande.typeDemande.libelle}</td>
                        <td>
                            <a class="btn btn-edit js-edit-btn"
                               href="/demande/modifier?id=${demande.id}"
                               data-can-edit="${canEditByDemandeId[demande.id]}">Modifier</a>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:when>
        <c:otherwise>
            <div class="empty-message">Aucune demande trouvee.</div>
        </c:otherwise>
    </c:choose>
</div>
<script>
    (function () {
        const editButtons = document.querySelectorAll('.js-edit-btn');
        const demandeRows = document.querySelectorAll('.js-demande-row');

        demandeRows.forEach(function (row) {
            row.addEventListener('click', function (event) {
                if (event.target.closest('a, button, input, select, textarea, label')) {
                    return;
                }

                const href = row.dataset.href;
                if (href) {
                    window.location.href = href;
                }
            });
        });

        editButtons.forEach(function (button) {
            button.addEventListener('click', function (event) {
                const canEdit = button.dataset.canEdit === 'true';
                if (canEdit) {
                    return;
                }

                event.preventDefault();
                button.textContent = 'Non modifiable';
                button.classList.add('is-disabled');
            });
        });
    })();
</script>