<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<div class="page">
    <div class="hero">
        <h1>Fiche de la demande #${demande.id}</h1>
        <p>Vue detaillee de la demande selectionnee.</p>
        <div class="badge">Consultation</div>
    </div>

    <div class="grid">
        <div class="card">
            <h2>Informations de la demande</h2>
            <div class="summary">
                <div class="item">
                    <span class="label">ID demande</span>
                    <span class="value">${demande.id}</span>
                </div>
                <div class="item">
                    <span class="label">Date demande</span>
                    <span class="value">${demande.dateDemande}</span>
                </div>
                <div class="item">
                    <span class="label">Type visa</span>
                    <span class="value">${demande.typeVisa.libelle}</span>
                </div>
                <div class="item">
                    <span class="label">Type demande</span>
                    <span class="value">${demande.typeDemande.libelle}</span>
                </div>
                <div class="item">
                    <span class="label">Modifiable</span>
                    <span class="value"><c:choose><c:when test="${canEdit}">Oui</c:when><c:otherwise>Non</c:otherwise></c:choose></span>
                </div>
                <div class="item">
                    <span class="label">Champs de dossier</span>
                    <span class="value">${selectedChampFournirIds.size()}</span>
                </div>
            </div>
        </div>

        <div class="card">
            <h2>Champs a fournir</h2>
            <c:choose>
                <c:when test="${not empty champsFournirWithStatus}">
                    <div class="summary">
                        <c:forEach var="champ" items="${champsFournirWithStatus}">
                            <div class="item item-span-2">
                                <span class="label">
                                    ${champ.libelle}
                                    <c:choose>
                                        <c:when test="${champ.isFourni}">
                                            <span style="color: green; margin-left: 10px;">✓ Fourni</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span style="color: red; margin-left: 10px;">✗ A fournir</span>
                                        </c:otherwise>
                                    </c:choose>
                                </span>
                            </div>
                        </c:forEach>
                    </div>
                </c:when>
                <c:otherwise>
                    <p>Aucun champ requis pour cette demande.</p>
                </c:otherwise>
            </c:choose>
        </div>

        <div class="card">
            <h2>Etat civil</h2>
            <div class="summary">
                <div class="item">
                    <span class="label">Nom</span>
                    <span class="value">${personne.nom}</span>
                </div>
                <div class="item">
                    <span class="label">Prenom</span>
                    <span class="value">${personne.prenom}</span>
                </div>
                <div class="item">
                    <span class="label">Date de naissance</span>
                    <span class="value">${personne.dateNaissance}</span>
                </div>
                <div class="item">
                    <span class="label">Telephone</span>
                    <span class="value">${personne.telephone}</span>
                </div>
                <div class="item">
                    <span class="label">Nationalite</span>
                    <span class="value">${personne.nationalite.libelle}</span>
                </div>
                <div class="item">
                    <span class="label">Situation familiale</span>
                    <span class="value">${personne.situationFamiliale.libelle}</span>
                </div>
                <div class="item item-span-2">
                    <span class="label">Adresse</span>
                    <span class="value">${personne.adresse}</span>
                </div>
            </div>
        </div>

        <div class="card">
            <h2>Passeport</h2>
            <div class="summary">
                <div class="item">
                    <span class="label">Numero passeport</span>
                    <span class="value">${passeport.numero}</span>
                </div>
                <div class="item">
                    <span class="label">Date d'expiration</span>
                    <span class="value">${passeport.dateExpiration}</span>
                </div>
                <div class="item">
                    <span class="label">Email</span>
                    <span class="value">${personne.email}</span>
                </div>
            </div>
        </div>

        <div class="card">
            <h2>Visa transformable</h2>
            <div class="summary">
                <div class="item">
                    <span class="label">Numero</span>
                    <span class="value">${visaTransformable.numero}</span>
                </div>
                <div class="item">
                    <span class="label">Date d'arrivee</span>
                    <span class="value">${visaTransformable.dateArrivee}</span>
                </div>
                <div class="item">
                    <span class="label">Date d'expiration</span>
                    <span class="value">${visaTransformable.dateExpiration}</span>
                </div>
            </div>
        </div>

        <div class="card">
            <h2>Code QR</h2>
            <div class="summary" style="text-align: center;">
                <c:if test="${demande != null && demande.id != null}">
                    <img src="/qrcode/${demande.id}" alt="QR Code de la demande" style="width: 200px; height: 200px; border: 1px solid #ddd; padding: 10px;" />
                    <p style="margin-top: 10px; color: #666;">Scannez ce code pour consulter votre demande</p>
                </c:if>
                <c:if test="${demande == null || demande.id == null}">
                    <p>Le QR Code n'est pas disponible pour cette demande.</p>
                </c:if>
            </div>
        </div>

        <div class="card">
            <h2>Photos & Signatures</h2>
            <div class="summary">
                <c:choose>
                    <c:when test="${not empty fichiersUplodes}">
                        <div style="display:flex;flex-wrap:wrap;gap:12px;">
                            <c:forEach items="${fichiersUplodes}" var="fichier">
                                <c:choose>
                                    <c:when test="${fn:endsWith(fichier.valeur, '.png') || fn:endsWith(fichier.valeur, '.jpg') || fn:endsWith(fichier.valeur, '.jpeg')}">
                                        <div style="width:220px;border:1px solid #e6e6e6;padding:8px;border-radius:8px;background:#fff;text-align:center;">
                                            <img src="/${fichier.valeur}" alt="Fichier" style="max-width:100%;height:auto;display:block;margin:0 auto 8px;"/>
                                            <div style="font-size:12px;color:#666;word-break:break-all;">${fichier.valeur}</div>
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <div style="padding:8px;border-radius:8px;background:#fff;border:1px solid #eee;">
                                            <a href="/${fichier.valeur}" target="_blank">Ouvrir le fichier</a>
                                            <div style="font-size:12px;color:#666;">${fichier.valeur}</div>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <p>Aucune photo ou signature n'a ete enregistree pour cette demande.</p>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <div class="card">
            <h2>Actions</h2>
            <div class="actions">
                <a class="btn btn-primary" href="/demandes">Retour a la liste</a>
                <c:if test="${canEdit}">
                    <a class="btn btn-secondary" href="/demande/modifier?id=${demande.id}">Modifier cette demande</a>
                </c:if>
            </div>
        </div>
    </div>
</div>
