package com.reedelk.salesforce.component;

import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.component.ProcessorSync;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.message.MessageAttributes;
import com.reedelk.runtime.api.message.MessageBuilder;
import com.reedelk.runtime.api.script.ScriptEngineService;
import com.reedelk.runtime.api.script.dynamicvalue.DynamicString;
import com.reedelk.salesforce.internal.commons.Messages;
import com.reedelk.salesforce.internal.exception.RecordGetException;
import com.reedelk.salesforce.internal.http.*;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.List;
import java.util.function.Function;

import static com.reedelk.runtime.api.commons.ComponentPrecondition.Configuration.requireNotBlank;
import static com.reedelk.runtime.api.commons.ComponentPrecondition.Configuration.requireNotNull;
import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@ModuleComponent("Salesforce Record Get")
@ComponentOutput(
        attributes = MessageAttributes.class,
        payload = String.class,
        description = "A JSON object containing the properties and values of the retrieved record with the given ID and Object Name.")
@ComponentInput(
        payload = Object.class,
        description = "The component input is used to evaluate the dynamic " +
                "values provided for the Object Name and Object ID of the record to be retrieved.")
@Description("The Salesforce Record Get Component returns a record of a Salesforce Object " +
        "given the object name and record ID. " +
        "Optionally it is possible to specify the fields to be returned in the record. " +
        "If the fields are empty all the fields for the object will be returned.")
@Component(service = RecordGet.class, scope = PROTOTYPE)
public class RecordGet implements ProcessorSync {

    @Property("Configuration")
    @Description("Salesforce authentication configuration.")
    private SalesforceConfiguration configuration;

    @Reference
    ScriptEngineService scriptService;

    @Override
    public void initialize() {
        requireNotNull(RecordGet.class, configuration, "Salesforce configuration must be provided.");
        configuration.validate(RecordGet.class);
        requireNotBlank(RecordGet.class, objectName, "Salesforce object name must be provided.");
    }

    @Override
    public Message apply(FlowContext flowContext, Message message) {

        String evaluatedObjectId = scriptService.evaluate(objectId, flowContext, message)
                .orElseThrow(() -> new RecordGetException(Messages.RecordGet.OBJECT_ID_EMPTY.format(objectId.value())));

        HttpBaseRequest request;
        if (fields == null || fields.isEmpty()) {
            request = new HttpRecordGet(configuration.getInstanceName(), objectName, evaluatedObjectId);
        } else {
            String joinedFields = String.join(",", fields);
            request = new HttpRecordGetWithFields(configuration.getInstanceName(), objectName, evaluatedObjectId, joinedFields);
        }

        String result = HttpAuthAwareRequestExecutor.execute(request, configuration, this, exceptionSupplier);

        return MessageBuilder.get(RecordGet.class)
                .withJson(result)
                .build();
    }

    @Override
    public void dispose() {
        HttpClientProvider.release(configuration, this);
    }

    private final Function<String, RecordGetException> exceptionSupplier = RecordGetException::new;

    public void setConfiguration(SalesforceConfiguration configuration) {
        this.configuration = configuration;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public void setObjectId(DynamicString objectId) {
        this.objectId = objectId;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    @Property("Object Name")
    @Hint("AccountOwnerSharingRule")
    @InitValue("Account")
    @Example("AccountContactRole")
    @Combo(editable = true, prototype = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX", comboValues = {
            "AcceptedEventRelation",
            "Account",
            "AccountBrand",
            "AccountContactRelation",
            "AccountCleanInfo",
            "AccountContactRole",
            "AccountInsight",
            "AccountOwnerSharingRule",
            "AccountPartner",
            "AccountRelationship",
            "AccountRelationshipShareRule",
            "AccountShare",
            "AccountTag",
            "AccountTeamMember",
            "AccountTerritoryAssignmentRule",
            "AccountTerritoryAssignmentRuleItem",
            "AccountTerritorySharingRule",
            "AccountUserTerritory2View",
            "ActionCadence",
            "ActionCadenceRule",
            "ActionCadenceRuleCondition",
            "ActionCadenceStep",
            "ActionCadenceStepTracker",
            "ActionCadenceTracker",
            "ActionLinkGroupTemplate",
            "ActionLinkTemplate",
            "ActionPlan",
            "ActionPlanItem",
            "ActionPlanTemplate",
            "ActionPlanTemplateItem",
            "ActionPlanTemplateItemValue",
            "ActionPlanTemplateVersion",
            "ActiveScratchOrg",
            "ActivityHistory",
            "ActivityMetric",
            "AdditionalNumber",
            "Address",
            "AgentWork",
            "AgentWorkSkill",
            "AIInsightAction",
            "AIInsightFeedback",
            "AIInsightReason",
            "AIInsightValue",
            "AIRecordInsight",
            "AllowedEmailDomain",
            "Announcement",
            "ApexClass",
            "ApexComponent",
            "ApexLog",
            "ApexPage",
            "ApexPageInfo",
            "ApexTestQueueItem",
            "ApexTestResult",
            "ApexTestResultLimits",
            "ApexTestRunResult",
            "ApexTestSuite",
            "ApexTrigger",
            "AppAnalyticsQueryRequest",
            "AppDefinition",
            "AppExtension",
            "AppMenuItem",
            "AppleDomainVerification",
            "AppointmentSchedulingPolicy",
            "Approval",
            "AppTabMember",
            "Article Type__DataCategorySelection",
            "Asset",
            "AssetDowntimePeriod",
            "AssetOwnerSharingRule",
            "AssetRelationship",
            "AssetShare",
            "AssetTag",
            "AssetTokenEvent",
            "AssignedResource",
            "AssignmentRule",
            "AssociatedLocation",
            "AsyncApexJob",
            "AttachedContentDocument",
            "AttachedContentNote",
            "Attachment",
            "Audience",
            "AuraDefinition",
            "AuraDefinitionBundle",
            "AuraDefinitionBundleInfo",
            "AuraDefinitionInfo",
            "AuthConfig",
            "AuthConfigProviders",
            "AuthorizationForm",
            "AuthorizationFormConsent",
            "AuthorizationFormDataUse",
            "AuthorizationFormText",
            "AuthProvider",
            "AuthSession",
            "BackgroundOperation",
            "BackgroundOperationResult",
            "BatchApexErrorEvent",
            "Bookmark",
            "BrandTemplate",
            "BusinessHours",
            "BusinessProcess",
            "BusinessProcessDefinition",
            "BusinessProcessFeedback",
            "BusinessProcessGroup",
            "BuyerGroupPricebook",
            "Calendar",
            "CalendarView",
            "CallCenter",
            "CallCoachConfigModifyEvent",
            "CallCoachingMediaProvider",
            "CallDisposition",
            "CallDispositionCategory",
            "CallTemplate",
            "Campaign",
            "CampaignInfluence",
            "CampaignInfluenceModel",
            "CampaignMember",
            "CampaignMemberStatus",
            "CampaignOwnerSharingRule",
            "CampaignShare",
            "CampaignTag",
            "CardPaymentMethod",
            "CartCheckoutSession",
            "CartDeliveryGroup",
            "CartDeliveryGroupMethod",
            "CartItem",
            "CartTax",
            "CartValidationOutput",
            "Case",
            "CaseArticle",
            "CaseComment",
            "CaseContactRole",
            "CaseHistory",
            "CaseMilestone",
            "CaseOwnerSharingRule",
            "CaseShare",
            "CaseSolution",
            "CaseStatus",
            "CaseSubjectParticle",
            "CaseTag",
            "CaseTeamMember",
            "CaseTeamRole",
            "CaseTeamTemplate",
            "CaseTeamTemplateMember",
            "CaseTeamTemplateRecord",
            "CategoryData",
            "CategoryNode",
            "CategoryNodeLocalization",
            "ChannelObjectLinkingRule",
            "ChannelProgram",
            "ChannelProgramLevel",
            "ChannelProgramMember",
            "ChatterActivity",
            "ChatterAnswersActivity",
            "ChatterAnswersReputationLevel",
            "ChatterConversation",
            "ChatterConversationMember",
            "ChatterMessage",
            "ClientBrowser",
            "CollaborationGroup",
            "CollaborationGroupMember",
            "CollaborationGroupMemberRequest",
            "CollaborationGroupRecord",
            "CollaborationInvitation",
            "ColorDefinition",
            "CombinedAttachment",
            "CommerceEntitlementBuyerGroup",
            "CommerceEntitlementPolicy",
            "CommerceEntitlementPolicyShare",
            "CommerceEntitlementProduct",
            "CommSubscription",
            "CommSubscriptionChannelType",
            "CommSubscriptionConsent",
            "CommSubscriptionTiming",
            "Community (Zone)",
            "ConnectedApplication",
            "Consumption Rate",
            "Consumption Schedule",
            "Contact",
            "ContactCleanInfo",
            "ContactPointAddress",
            "ContactPointConsent",
            "ContactPointEmail",
            "ContactPointPhone",
            "ContactPointTypeConsent",
            "ContactOwnerSharingRule",
            "ContactRequest",
            "ContactRequestShare",
            "ContactShare",
            "ContactSuggestionInsight",
            "ContactTag",
            "ContentAsset",
            "ContentBody",
            "ContentDistribution",
            "ContentDistributionView",
            "ContentDocument",
            "ContentDocumentHistory",
            "ContentDocumentLink",
            "ContentDocumentListViewMapping",
            "ContentDocumentSubscription",
            "ContentFolder",
            "ContentFolderItem",
            "ContentFolderLink",
            "ContentFolderMember",
            "ContentHubItem",
            "ContentHubRepository",
            "ContentNote",
            "ContentNotification",
            "ContentTagSubscription",
            "ContentUserSubscription",
            "ContentVersion",
            "ContentVersionComment",
            "ContentVersionHistory",
            "ContentVersionRating",
            "ContentWorkspace",
            "ContentWorkspaceDoc",
            "ContentWorkspaceMember",
            "ContentWorkspacePermission",
            "ContentWorkspaceSubscription",
            "Contract",
            "ContractContactRole",
            "ContractLineItem",
            "ContractStatus",
            "ContractTag",
            "Conversation",
            "ConversationContextEntry",
            "ConversationEntry",
            "ConversationParticipant",
            "CorsWhitelistEntry",
            "CreditMemo",
            "CreditMemoLine",
            "Crisis",
            "CronJobDetail",
            "CronTrigger",
            "CspTrustedSite",
            "CurrencyType",
            "CustomBrand",
            "CustomBrandAsset",
            "CustomHelpMenuItem",
            "CustomHelpMenuSection",
            "CustomHttpHeader",
            "CustomNotificationType",
            "CustomPermission",
            "CustomPermissionDependency",
            "DandBCompany",
            "Dashboard",
            "DashboardComponent",
            "DashboardTag",
            "DataAssessmentFieldMetric",
            "DataAssessmentMetric",
            "DataAssessmentValueMetric",
            "DatacloudCompany",
            "DatacloudContact",
            "DatacloudDandBCompany",
            "DatacloudOwnedEntity",
            "DatacloudPurchaseUsage",
            "DatacloudSocialHandle",
            "DataIntegrationRecordPurchasePermission",
            "DatasetExport",
            "DatasetExportPart",
            "DataUseLegalBasis",
            "DataUsePurpose",
            "DatedConversionRate",
            "DcSocialProfile",
            "DcSocialProfileHandle",
            "DeclinedEventRelation",
            "DelegatedAccount",
            "DeleteEvent",
            "DigitalSignature",
            "DigitalWallet",
            "DirectMessage",
            "Division",
            "DivisionLocalization",
            "Document",
            "DocumentAttachmentMap",
            "DocumentTag",
            "Domain",
            "DomainSite",
            "DuplicateJob",
            "DuplicateJobDefinition",
            "DuplicateJobMatchingRule",
            "DuplicateJobMatchingRuleDefinition",
            "DuplicateRecordItem",
            "DuplicateRecordSet",
            "DuplicateRule",
            "ElectronicMediaGroup",
            "ElectronicMediaUse",
            "EmailDomainFilter",
            "EmailDomainKey",
            "EmailMessage",
            "EmailMessageRelation",
            "EmailRelay",
            "EmailServicesAddress",
            "EmailServicesFunction",
            "EmailStatus",
            "EmailTemplate",
            "EmbeddedServiceDetail",
            "Employee",
            "EmployeeCrisisAssessment",
            "EngagementChannelType",
            "EmbeddedServiceLabel",
            "EnhancedLetterhead",
            "Entitlement",
            "EntitlementContact",
            "EntitlementTemplate",
            "EntityHistory",
            "EntityMilestone",
            "EntitySubscription",
            "Expense",
            "EnvironmentHubMember",
            "Event",
            "EventLogFile",
            "EventRelation",
            "EventBusSubscriber",
            "EventTag",
            "EventWhoRelation",
            "ExpressionFilter",
            "ExpressionFilterCriteria",
            "ExternalAccountHierarchy",
            "ExternalDataSource",
            "ExternalDataUserAuth",
            "ExternalSocialAccount",
            "FeedAttachment",
            "FeedComment",
            "FeedItem",
            "FeedLike",
            "FeedPollChoice",
            "FeedPollVote",
            "FeedPost",
            "FeedRevision",
            "FeedTrackedChange",
            "FieldHistoryArchive",
            "FieldPermissions",
            "FieldSecurityClassification",
            "FieldServiceMobileSettings",
            "FiscalYearSettings",
            "FlexQueueItem",
            "FlowDefinitionView",
            "FlowInterview",
            "FlowInterviewOwnerSharingRule",
            "FlowInterviewShare",
            "FlowRecordRelation",
            "FlowStageRelation",
            "FlowVariableView",
            "FlowVersionView",
            "Folder",
            "FolderedContentDocument",
            "ForecastingAdjustment",
            "ForecastingDisplayedFamily",
            "ForecastingFact",
            "ForecastingItem",
            "ForecastingOwnerAdjustment",
            "ForecastingQuota",
            "ForecastingShare",
            "ForecastingType",
            "ForecastingUserPreference",
            "FormulaFunction",
            "FormulaFunctionAllowedType",
            "FormulaFunctionCategory",
            "FulfillmentOrder",
            "FulfillmentOrderItemAdjustment",
            "FulfillmentOrderItemTax",
            "FulfillmentOrderLineItem",
            "Goal",
            "GoalLink",
            "GoogleDoc",
            "Group",
            "GroupMember",
            "HashtagDefinition",
            "HealthCareDiagnosis",
            "HealthCareProcedure",
            "Holiday",
            "IconDefinition",
            "Idea",
            "IdeaComment",
            "IdeaReputation",
            "IdeaReputationLevel",
            "IdeaTheme",
            "IdpEventLog",
            "IframeWhiteListUrl",
            "Image",
            "Individual",
            "IndividualHistory",
            "IndividualShare",
            "InternalOrganizationUnit",
            "Invoice",
            "InvoiceLine",
            "JobProfile",
            "Knowledge__Feed",
            "Knowledge__ka",
            "Knowledge__kav",
            "Knowledge__DataCategorySelection",
            "KnowledgeableUser",
            "KnowledgeArticle",
            "KnowledgeArticleVersion",
            "KnowledgeArticleVersionHistory",
            "KnowledgeArticleViewStat",
            "KnowledgeArticleVoteStat",
            "LandingPage",
            "Lead",
            "LeadCleanInfo",
            "LeadOwnerSharingRule",
            "LeadShare",
            "LeadStatus",
            "LeadTag",
            "LegalEntity",
            "LightningExperienceTheme",
            "LightningOnboardingConfig",
            "LightningToggleMetrics",
            "LightningUsageByAppTypeMetrics",
            "LightningUsageByBrowserMetrics",
            "LightningUsageByPageMetrics",
            "LightningUsageByFlexiPageMetrics",
            "LightningExitByPageMetrics",
            "LinkedArticle",
            "LinkedArticleFeed",
            "LinkedArticleHistory",
            "ListEmail",
            "ListEmailIndividualRecipient",
            "ListEmailRecipientSource",
            "ListView",
            "ListViewChart",
            "ListViewChartInstance",
            "LiveAgentSession",
            "LiveAgentSessionHistory",
            "LiveAgentSessionShare",
            "LiveChatBlockingRule",
            "LiveChatButton",
            "LiveChatButtonDeployment",
            "LiveChatButtonSkill",
            "LiveChatDeployment",
            "LiveChatSensitiveDataRule",
            "LiveChatTranscript",
            "LiveChatTranscriptChangeEvent",
            "LiveChatTranscriptEvent",
            "LiveChatTranscriptShare",
            "LiveChatTranscriptSkill",
            "LiveChatUserConfig",
            "LiveChatUserConfigProfile",
            "LiveChatUserConfigUser",
            "LiveChatVisitor",
            "Location",
            "LoginEvent",
            "LoginGeo",
            "LoginHistory",
            "LoginIp",
            "LogoutEventStream",
            "LookedUpFromActivity",
            "Macro",
            "MacroInstruction",
            "MacroUsage",
            "MailmergeTemplate",
            "MaintenanceAsset",
            "MaintenancePlan",
            "MarketingForm",
            "MarketingLink",
            "MatchingRule",
            "MatchingRuleItem",
            "MessagingChannel",
            "MessagingChannelSkill",
            "MessagingConfiguration",
            "MessagingDeliveryError",
            "MessagingEndUser",
            "MessagingLink",
            "MessagingSession",
            "MessagingTemplate",
            "MetadataPackage",
            "MetadataPackageVersion",
            "Metric",
            "MetricDataLink",
            "MetricsDataFile",
            "MilestoneType",
            "MobileSettingsAssignment",
            "MsgChannelLanguageKeyword",
            "MyDomainDiscoverableLogin",
            "MutingPermissionSet",
            "Name",
            "NamedCredential",
            "NamespaceRegistry",
            "NavigationLinkSet",
            "NavigationMenuItem",
            "NavigationMenuItemLocalization",
            "Network",
            "NetworkActivityAudit",
            "NetworkAffinity",
            "NetworkDiscoverableLogin",
            "NetworkMember",
            "NetworkMemberGroup",
            "NetworkModeration",
            "NetworkPageOverride",
            "NetworkSelfRegistration",
            "NetworkUserHistoryRecent",
            "Note",
            "NoteAndAttachment",
            "NoteTag",
            "OauthCustomScope",
            "OauthCustomScopeApp",
            "OauthToken",
            "ObjectPermissions",
            "ObjectTerritory2AssignmentRule",
            "ObjectTerritory2AssignmentRuleItem",
            "ObjectTerritory2Association",
            "OpenActivity",
            "OperatingHours",
            "OperatingHoursHistory",
            "Opportunity",
            "OpportunityCompetitor",
            "OpportunityContactRole",
            "OpportunityContactRoleSuggestionInsight",
            "OpportunityFieldHistory",
            "OpportunityHistory",
            "OpportunityInsight",
            "OpportunityLineItem",
            "OpportunityLineItemSchedule",
            "OpportunityOwnerSharingRule",
            "OpportunityPartner",
            "OpportunityShare",
            "OpportunitySplit",
            "OpportunitySplitType",
            "OpportunityStage",
            "OpportunityTag",
            "OpportunityTeamMember",
            "Order",
            "OrderAdjustmentGroup",
            "OrderAdjustmentGroupSummary",
            "OrderDeliveryGroup",
            "OrderDeliveryGroupSummary",
            "OrderDeliveryMethod",
            "OrderHistory",
            "OrderItem",
            "OrderItemAdjustmentLineItem",
            "OrderItemAdjustmentLineSummary",
            "OrderItemSummary",
            "OrderItemSummaryChange",
            "OrderItemTaxLineItem",
            "OrderItemTaxLineItemSummary",
            "OrderOwnerSharingRule",
            "OrderPaymentSummary",
            "OrderSummary",
            "Organization",
            "OrgDeleteRequest",
            "OrgWideEmailAddress",
            "OutOfOffice",
            "OwnedContentDocument",
            "OwnerChangeOptionInfo",
            "PackageLicense",
            "PackagePushError",
            "PackagePushJob",
            "PackagePushRequest",
            "PackageSubscriber",
            "Partner",
            "PartnerFundAllocation",
            "PartnerFundClaim",
            "PartnerFundRequest",
            "PartnerMarketingBudget",
            "PartnerNetworkConnection",
            "PartnerNetworkRecordConnection",
            "PartnerNetworkSyncLog",
            "PartnerRole",
            "PartyConsent",
            "Payment",
            "PaymentAuthorization",
            "PaymentGateway",
            "PaymentGatewayLog",
            "PaymentGatewayProvider",
            "PaymentGroup",
            "PaymentLineInvoice",
            "PaymentMethod",
            "PendingServiceRouting",
            "Period",
            "PermissionSet",
            "PermissionSetAssignment",
            "PermissionSetGroup",
            "PermissionSetGroupComponent",
            "PermissionSetLicense",
            "PermissionSetLicenseAssign",
            "PermissionSetTabSetting",
            "PersonalizationTargetInfo",
            "PlatformAction",
            "PlatformStatusAlertEvent",
            "PortalDelegablePermissionSet",
            "PresenceConfigDeclineReason",
            "PresenceDeclineReason",
            "PresenceUserConfig",
            "PresenceUserConfigProfile",
            "PresenceUserConfigUser",
            "Pricebook2",
            "Pricebook2History",
            "PricebookEntry",
            "ProcessDefinition",
            "ProcessInstance",
            "ProcessInstanceHistory",
            "ProcessInstanceStep",
            "ProcessInstanceNode",
            "ProcessInstanceWorkitem",
            "ProcessNode",
            "Product2",
            "Product2DataTranslation",
            "ProductCategory",
            "ProductCategoryDataTranslation",
            "ProductConsumed",
            "ProductEntitlementTemplate",
            "ProductItem",
            "ProductItemTransaction",
            "ProductMedia",
            "ProductRequest",
            "ProductRequestLineItem",
            "ProductRequired",
            "ProductTransfer",
            "Profile",
            "ProfileSkill",
            "ProfileSkillEndorsement",
            "ProfileSkillShare",
            "ProfileSkillUser",
            "Prompt",
            "PromptAction",
            "PromptActionOwnerSharingRule",
            "PromptActionShare",
            "PromptLocalization",
            "PromptVersion",
            "PromptVersionLocalization",
            "Reference: PushTopic",
            "QueueRoutingConfig",
            "Question",
            "QuestionDataCategorySelection",
            "QuestionReportAbuse",
            "QuestionSubscription",
            "QueueSobject",
            "QuickText",
            "QuickTextUsage",
            "Quote",
            "QuoteDocument",
            "QuoteLineItem",
            "RecentlyViewed",
            "Recommendation",
            "RecordAction",
            "RecordActionHistory",
            "RecordType",
            "RecordTypeLocalization",
            "RecordVisibility (Pilot)",
            "RedirectWhitelistUrl",
            "Refund",
            "RefundLinePayment",
            "RegisteredExternalService",
            "RemoteKeyCalloutEvent",
            "Reply",
            "ReplyReportAbuse",
            "ReplyText",
            "Report",
            "ReportTag",
            "ReputationLevel",
            "ReputationLevelLocalization",
            "ReputationPointsRule",
            "ResourceAbsence",
            "ResourcePreference",
            "ReturnOrder",
            "ReturnOrderLineItem",
            "ReturnOrderOwnerSharingRule",
            "RuleTerritory2Association",
            "SalesAIScoreCycle",
            "SalesAIScoreModelFactor",
            "SalesChannel",
            "SalesWorkQueueSettings",
            "SamlSsoConfig",
            "Scontrol",
            "ScontrolLocalization",
            "ScratchOrgInfo",
            "SearchPromotionRule",
            "SecureAgent",
            "SecureAgentsCluster",
            "SecurityCustomBaseline",
            "SelfServiceUser",
            "ServiceAppointment",
            "ServiceAppointmentStatus",
            "ServiceChannel",
            "ServiceChannelFieldPriority",
            "ServiceChannelStatus",
            "ServiceChannelStatusField",
            "ServiceContract",
            "ServiceContractOwnerSharingRule",
            "ServiceCrew",
            "ServiceCrewMember",
            "ServiceCrewOwnerSharingRule",
            "ServicePresenceStatus",
            "ServiceReport",
            "ServiceReportLayout",
            "ServiceResource",
            "ServiceResourceCapacity",
            "ServiceResourceCapacityHistory",
            "ServiceResourceOwnerSharingRule",
            "ServiceResourceSkill",
            "ServiceTerritory",
            "ServiceTerritoryLocation",
            "ServiceTerritoryMember",
            "ServiceTerritoryWorkType",
            "SessionPermSetActivation",
            "SetupAuditTrail",
            "SetupEntityAccess",
            "Shift",
            "ShiftHistory",
            "ShiftOwnerSharingRule",
            "ShiftShare",
            "ShiftStatus",
            "Shipment",
            "SignupRequest",
            "Site",
            "SiteDetail",
            "SiteDomain",
            "SiteHistory",
            "SiteIframeWhitelistUrl",
            "Skill",
            "SkillProfile",
            "SkillRequirement",
            "SkillUser",
            "SlaProcess",
            "Snippet",
            "SnippetAssignment",
            "SocialPersona",
            "SocialPost",
            "Solution",
            "SolutionStatus",
            "SolutionTag",
            "SOSDeployment",
            "SOSSession",
            "SOSSessionActivity",
            "Stamp",
            "StampAssignment",
            "StaticResource",
            "StoreIntegratedService",
            "Reference: StreamingChannel",
            "Salesforce Surveys Object Model",
            "Survey",
            "SurveyEmailBranding",
            "SurveyEngagementContext",
            "SurveyInvitation",
            "SurveyPage",
            "SurveyQuestion",
            "SurveyQuestionChoice",
            "SurveyQuestionResponse",
            "SurveyQuestionScore",
            "SurveyResponse",
            "SurveySubject",
            "SurveyVersion",
            "SurveyVersionAddlInfo",
            "TabDefinition",
            "TagDefinition",
            "Task",
            "TaskPriority",
            "TaskRelation",
            "TaskStatus",
            "TaskTag",
            "TaskWhoRelation",
            "TenantSecret",
            "Territory",
            "Territory2",
            "Territory2Model",
            "Territory2ModelHistory",
            "Territory2Type",
            "TestSuiteMembership",
            "ThirdPartyAccountLink",
            "ThreatDetectionFeedback",
            "TimeSheet",
            "TimeSheetEntry",
            "TimeSlot",
            "TimeSlotHistory",
            "Topic",
            "TopicAssignment",
            "TopicLocalization—Beta",
            "TopicUserEvent",
            "TransactionSecurityPolicy",
            "TwoFactorInfo",
            "TwoFactorMethodsInfo",
            "TwoFactorTempCode",
            "UiFormulaCriterion",
            "UiFormulaRule",
            "UndecidedEventRelation",
            "User",
            "UserAccountTeamMember",
            "UserAppInfo",
            "UserAppMenuCustomization",
            "UserAppMenuItem",
            "UserAuthCertificate",
            "UserConfigTransferButton",
            "UserConfigTransferSkill",
            "UserCustomBadge",
            "UserCustomBadgeLocalization",
            "UserDevice",
            "UserDeviceApplication",
            "UserEmailCalendarSync",
            "UserEmailPreferredPerson",
            "UserEmailPreferredPersonShare",
            "UserLicense",
            "UserListView",
            "UserListViewCriterion",
            "UserLogin",
            "UserMembershipSharingRule",
            "UserPackageLicense",
            "UserPermissionAccess",
            "UserPreference",
            "UserProfile",
            "UserProvAccount",
            "UserProvAccountStaging",
            "UserProvMockTarget",
            "UserProvisioningConfig",
            "UserProvisioningLog",
            "UserProvisioningRequest",
            "UserRecordAccess",
            "UserRole",
            "UserServicePresence",
            "UserShare",
            "UserTeamMember",
            "UserTerritory",
            "UserTerritory2Association",
            "UserWorkList",
            "UserWorkListItem",
            "VerificationHistory",
            "VisualforceAccessMetrics",
            "VoiceCall",
            "VoiceCallList",
            "VoiceCallListItem",
            "VoiceCallQualityFeedback",
            "VoiceCallRecording",
            "VoiceCoaching",
            "VoiceLocalPresenceNumber",
            "VoiceMailContent",
            "VoiceMailGreeting",
            "VoiceMailMessage",
            "VoiceUserLine",
            "VoiceUserPreferences",
            "VoiceVendorInfo",
            "VoiceVendorLine",
            "Vote",
            "WaveAutoInstallRequest",
            "WebCart",
            "WebCartHistory",
            "WebLink",
            "WebLinkLocalization",
            "WebStore",
            "WebStoreCatalog",
            "WebStorePricebook",
            "Wishlist (Beta)",
            "WishlistItem (Beta)",
            "WorkAccess",
            "WorkAccessShare",
            "WorkBadge",
            "WorkBadgeDefinition",
            "WorkCoaching",
            "WorkFeedback",
            "WorkFeedbackQuestion",
            "WorkFeedbackQuestionSet",
            "WorkFeedbackRequest",
            "WorkGoal",
            "WorkGoalCollaborator",
            "WorkGoalCollaboratorHistory",
            "WorkGoalHistory",
            "WorkGoalLink",
            "WorkGoalShare",
            "WorkOrder",
            "WorkOrderHistory",
            "WorkOrderLineItem",
            "WorkOrderLineItemHistory",
            "WorkOrderLineItemStatus",
            "WorkOrderShare",
            "WorkOrderStatus",
            "WorkPerformanceCycle",
            "WorkReward",
            "WorkRewardFund",
            "WorkRewardFundType",
            "WorkThanks",
            "WorkType",
            "WorkTypeGroup",
            "WorkTypeGroupMember"})
    @Description("The name of the Salesforce object to be returned.")
    private String objectName;

    @Property("Object ID")
    @Hint("001D000000INjVe")
    @Example("001D000000INjVe")
    @Description("The id of the Salesforce object to be returned.")
    private DynamicString objectId;

    @Property("Return Fields")
    @TabGroup("Return Fields")
    @Description("The fields to be returned from the Object with the given name and ID. " +
            "If empty all the fields will be returned.")
    private List<String> fields;
}
