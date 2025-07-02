package com.example.uiclient.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DetailNewsResponse {

    @SerializedName("candidates")
    private List<Candidate> candidates;

    @SerializedName("usageMetadata")
    private UsageMetadata usageMetadata;

    @SerializedName("modelVersion")
    private String modelVersion;

    @SerializedName("responseId")
    private String responseId;

    public List<Candidate> getCandidates() {
        return candidates;
    }

    public UsageMetadata getUsageMetadata() {
        return usageMetadata;
    }

    public String getModelVersion() {
        return modelVersion;
    }

    public String getResponseId() {
        return responseId;
    }

    public static class Candidate {
        @SerializedName("content")
        private Content content;

        @SerializedName("finishReason")
        private String finishReason;

        @SerializedName("avgLogprobs")
        private double avgLogprobs;

        public Content getContent() {
            return content;
        }

        public String getFinishReason() {
            return finishReason;
        }

        public double getAvgLogprobs() {
            return avgLogprobs;
        }
    }

    public static class Content {
        @SerializedName("parts")
        private List<Part> parts;

        @SerializedName("role")
        private String role;

        public List<Part> getParts() {
            return parts;
        }

        public String getRole() {
            return role;
        }
    }

    public static class Part {
        @SerializedName("text")
        private String text;

        public String getText() {
            return text;
        }
    }

    public static class UsageMetadata {
        @SerializedName("promptTokenCount")
        private int promptTokenCount;

        @SerializedName("candidatesTokenCount")
        private int candidatesTokenCount;

        @SerializedName("totalTokenCount")
        private int totalTokenCount;

        @SerializedName("promptTokensDetails")
        private List<TokenDetail> promptTokensDetails;

        @SerializedName("candidatesTokensDetails")
        private List<TokenDetail> candidatesTokensDetails;

        public int getPromptTokenCount() {
            return promptTokenCount;
        }

        public int getCandidatesTokenCount() {
            return candidatesTokenCount;
        }

        public int getTotalTokenCount() {
            return totalTokenCount;
        }

        public List<TokenDetail> getPromptTokensDetails() {
            return promptTokensDetails;
        }

        public List<TokenDetail> getCandidatesTokensDetails() {
            return candidatesTokensDetails;
        }
    }

    public static class TokenDetail {
        @SerializedName("modality")
        private String modality;

        @SerializedName("tokenCount")
        private int tokenCount;

        public String getModality() {
            return modality;
        }

        public int getTokenCount() {
            return tokenCount;
        }
    }
}