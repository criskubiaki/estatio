/*
 *
 *  Copyright 2012-2014 Eurocommercial Properties NV
 *
 *
 *  Licensed under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.estatio.fixture.documents;

import javax.inject.Inject;

import org.joda.time.LocalDate;

import org.apache.isis.applib.services.clock.ClockService;

import org.isisaddons.module.security.dom.tenancy.ApplicationTenancy;
import org.isisaddons.module.security.dom.tenancy.ApplicationTenancyRepository;

import org.incode.module.documents.dom.impl.docs.DocumentTemplate;
import org.incode.module.documents.dom.impl.rendering.RenderingStrategy;
import org.incode.module.documents.dom.impl.rendering.RenderingStrategyRepository;
import org.incode.module.documents.dom.impl.types.DocumentType;
import org.incode.module.documents.fixture.DocumentTemplateFSAbstract;

import org.estatio.dom.documents.binders.BinderForReportServer;
import org.estatio.dom.documents.binders.BinderForReportServerForInvoiceSummaryForPropertyDueDateStatus;
import org.estatio.dom.invoice.Invoice;
import org.estatio.dom.invoice.viewmodel.InvoiceSummaryForPropertyDueDateStatus;
import org.estatio.fixture.security.tenancy.ApplicationTenancyForIt;

public class DocumentTypeAndTemplatesFSForItalianInvoicesUsingSsrs extends DocumentTemplateFSAbstract {

    public static final String AT_PATH = ApplicationTenancyForIt.PATH;

    // applicable to Invoice.class
    public static final String DOC_TYPE_REF_INVOICE_PRELIM_ITA = "INVOICE-PRELIM/ITA";
    public static final String DOC_TYPE_REF_INVOICE_ITA = "INVOICE/ITA";

    // applicable to InvoiceSummaryForPropertyDueDateStatus.class
    public static final String DOC_TYPE_REF_INVOICES_OVERVIEW_ITA = "INVOICES/ITA";
    public static final String DOC_TYPE_REF_INVOICES_PRELIM_ITA = "INVOICES-PRELIM/ITA";
    public static final String DOC_TYPE_REF_INVOICES_PRELIM_FOR_SELLER_ITA = "INVOICES-FOR-SELLER/ITA";

    @Override
    protected void execute(final ExecutionContext executionContext) {

        // prereqs
        executionContext.executeChild(this, new RenderingStrategyFSForSsrs());

        final String url = "${reportServerBaseUrl}";

        final RenderingStrategy renderingStrategy =
                renderingStrategyRepository.findByReference(RenderingStrategyFSForSsrs.REF);

        final DocumentTemplate prelimInvoiceTemplate = createTypeAndTemplate(
                DOC_TYPE_REF_INVOICE_PRELIM_ITA,
                "Preliminary letter for Invoice (Italy)",
                AT_PATH,
                renderingStrategy,
                url
                        + "Preliminary+Letter"
                        + "&id=${this.id}"
                        + "&rs:Command=Render",
                executionContext);

        final DocumentTemplate italyInvoiceTemplate = createTypeAndTemplate(
                DOC_TYPE_REF_INVOICE_ITA,
                "Invoice (Italy)",
                AT_PATH,
                renderingStrategy,
                url
                        + "Invoice"
                        + "&id=${this.id}"
                        + "&rs:Command=Render",
                executionContext);

        prelimInvoiceTemplate.applicable(
                Invoice.class.getName(), BinderForReportServer.class.getName());
        italyInvoiceTemplate.applicable(
                Invoice.class.getName(), BinderForReportServer.class.getName());


        final DocumentTemplate italyInvoicesOverviewTemplate = createTypeAndTemplate(
                DOC_TYPE_REF_INVOICES_OVERVIEW_ITA,
                "Invoices overview (Italy)",
                AT_PATH,
                renderingStrategy,
                url
                        + "Invoices"
                        + "&dueDate=${this.dueDate}&${this.seller.id}&atPath=${this.atPath}"
                        + "&rs:Command=Render",
                executionContext);

        final DocumentTemplate italyPrelimInvoicesTemplate = createTypeAndTemplate(
                DOC_TYPE_REF_INVOICES_PRELIM_ITA,
                "Preliminary letter for Invoices (Italy)",
                AT_PATH,
                renderingStrategy,
                url
                        + "Preliminary+Letter"
                        + "&dueDate=${this.dueDate}&sellerId=${this.seller.id}&atPath=${this.atPath}"
                        + "&rs:Command=Render",
                executionContext);

        final DocumentTemplate italyInvoicesForSellerTemplate = createTypeAndTemplate(
                DOC_TYPE_REF_INVOICES_PRELIM_FOR_SELLER_ITA,
                "Invoice for Seller (Italy)",
                AT_PATH,
                renderingStrategy,
                url
                        + "Preliminary+Letter"
                        + "&dueDate=${this.dueDate}&sellerId=${this.seller.id}&atPath=${this.atPath}"
                        + "&rs:Command=Render",
                executionContext);

        italyInvoicesOverviewTemplate.applicable(
                InvoiceSummaryForPropertyDueDateStatus.class.getName(),
                BinderForReportServerForInvoiceSummaryForPropertyDueDateStatus.class.getName());
        italyPrelimInvoicesTemplate.applicable(
                InvoiceSummaryForPropertyDueDateStatus.class.getName(),
                BinderForReportServerForInvoiceSummaryForPropertyDueDateStatus.class.getName());
        italyInvoicesForSellerTemplate.applicable(
                InvoiceSummaryForPropertyDueDateStatus.class.getName(),
                BinderForReportServerForInvoiceSummaryForPropertyDueDateStatus.class.getName());
    }

    protected DocumentTemplate createTypeAndTemplate(
            final String docTypeRef,
            final String docTypeName,
            final String atPath,
            final RenderingStrategy renderingStrategy,
            final String templateText,
            final ExecutionContext executionContext) {


        final DocumentType docType = documentTypeRepository.create(docTypeRef, docTypeName);

        final LocalDate now = clockService.now();

        final ApplicationTenancy appTenancy = applicationTenancyRepository.findByPath(atPath);

        return createDocumentTextTemplate(
                docType, now, docType.getName(),
                "application/pdf",
                ".pdf",
                appTenancy.getPath(),
                templateText,
                renderingStrategy,
                executionContext);
    }

    @Inject
    private ApplicationTenancyRepository applicationTenancyRepository;
    @Inject
    private RenderingStrategyRepository renderingStrategyRepository;
    @Inject
    private ClockService clockService;


}