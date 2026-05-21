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
                    <th>Statut</th>
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
                        <td>${statutByDemandeId[demande.id]}</td>
                        <td>
                            <a class="btn btn-edit" href="/demande/photo-signature?id=${demande.id}">Photo et signature</a>
                            
                            <button class="btn btn-secondary js-view-btn" data-demande-id="${demande.id}">Visualiser</button>

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
<!-- Attachments modal -->
<div id="attachmentsModalBackdrop" style="display:none; position:fixed; inset:0; background:rgba(0,0,0,0.5); z-index:9998"></div>
<div id="attachmentsModal" style="display:none; position:fixed; inset:10% 20%; background:#fff; z-index:9999; padding:16px; overflow:auto; border-radius:6px; max-height:80%;">
    <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom:8px;">
        <h2 style="margin:0">Visualisation de tout les fichiers du dossier</h2>
        <button id="attachmentsModalClose" style="font-size:20px; background:none; border:none;">&times;</button>
    </div>
    <div id="attachmentsList"></div>
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
                console.log('Can edit:', canEdit, 'for button with href:', button.getAttribute('href'));
                if (canEdit) {
                    console.log('Modification autorisee pour cette demande.');
                    return;
                }

                event.preventDefault();
                button.textContent = 'Non modifiable';
                console.log('Modification interdite: le type_statut_demande doit etre egal a 1 ou 2.');
                button.classList.add('is-disabled');
            });
        });
    })();
</script>
<script>
    (function () {
        function showModal() {
            document.getElementById('attachmentsModalBackdrop').style.display = 'block';
            document.getElementById('attachmentsModal').style.display = 'block';
        }

        function hideModal() {
            document.getElementById('attachmentsModalBackdrop').style.display = 'none';
            document.getElementById('attachmentsModal').style.display = 'none';
            document.getElementById('attachmentsList').innerHTML = '';
        }

        document.getElementById('attachmentsModalClose').addEventListener('click', hideModal);
        document.getElementById('attachmentsModalBackdrop').addEventListener('click', hideModal);

        document.querySelectorAll('.js-view-btn').forEach(function (btn) {
            btn.addEventListener('click', function (event) {
                event.preventDefault();
                event.stopPropagation();
                var demandeId = btn.dataset.demandeId;
                if (!demandeId) return;
                fetch('/demande/attachments?demandeId=' + encodeURIComponent(demandeId))
                    .then(function (res) { return res.json(); })
                    .then(function (files) {
                        var container = document.getElementById('attachmentsList');
                        container.innerHTML = '';
                        if (!files || files.length === 0) {
                            container.innerHTML = '<p>Aucun fichier trouve pour cette demande.</p>';
                            showModal();
                            return;
                        }

                        files.forEach(function (f) {
                            var row = document.createElement('div');
                            row.style.marginBottom = '12px';

                            // Determine a friendly label from dossierValeur
                            var labelText = 'Fichier';
                            if (f.dossierValeur) {
                                var dv = f.dossierValeur.toString();
                                var v = dv.toLowerCase();
                                if (v.indexOf('image') !== -1) {
                                    labelText = 'Photo de la personne';
                                } else if (v.indexOf('signature') !== -1) {
                                    labelText = 'Signature de la personne';
                                } else {
                                    labelText = dv;
                                }
                            }

                            var label = document.createElement('div');
                            label.textContent = labelText;
                            label.style.fontWeight = '600';
                            label.style.marginBottom = '6px';
                            row.appendChild(label);

                            if (f.kind === 'image') {
                                var img = document.createElement('img');
                                img.src = f.url;
                                img.style.maxWidth = '500px';
                                img.style.maxHeight = '500px';
                                img.style.display = 'block';
                                img.style.marginBottom = '6px';
                                row.appendChild(img);
                                var link = document.createElement('a');
                                link.href = f.url;
                                link.target = '_blank';
                                link.textContent = f.filename || 'Ouvrir l\'image';
                                row.appendChild(link);
                            } else {
                                var link = document.createElement('a');
                                link.href = f.url;
                                link.target = '_blank';
                                link.textContent = f.filename || 'Ouvrir le fichier';
                                row.appendChild(link);
                            }

                            container.appendChild(row);
                        });

                        showModal();
                    })
                    .catch(function (err) {
                        console.error('Erreur en recuperant les fichiers:', err);
                        alert('Erreur lors de la récupération des fichiers.');
                    });
            });
        });
    })();
</script>