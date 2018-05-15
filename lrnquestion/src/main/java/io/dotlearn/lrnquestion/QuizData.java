package io.dotlearn.lrnquestion;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

public class QuizData implements Parcelable {

    public int answer;
    public String explanation;
    public Hint hint;
    public String prompt;
    public List<Option> options;
    public String image;

    public QuizData() {}

    public QuizData(int answer, @Nullable String explanation, @Nullable Hint hint,
                    @NonNull String prompt, @NonNull List<Option> options, @Nullable String image) {
        this.answer = answer;
        this.explanation = explanation;
        this.hint = hint;
        this.prompt = prompt;
        this.options = options;
        this.image = image;
    }

    protected QuizData(Parcel in) {
        answer = in.readInt();
        explanation = in.readString();
        hint = in.readParcelable(Hint.class.getClassLoader());
        prompt = in.readString();
        options = in.createTypedArrayList(Option.CREATOR);
        image = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(answer);
        dest.writeString(explanation);
        dest.writeParcelable(hint, flags);
        dest.writeString(prompt);
        dest.writeTypedList(options);
        dest.writeString(image);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<QuizData> CREATOR = new Creator<QuizData>() {
        @Override
        public QuizData createFromParcel(Parcel in) {
            return new QuizData(in);
        }

        @Override
        public QuizData[] newArray(int size) {
            return new QuizData[size];
        }
    };

    public static class Hint implements Parcelable {

        public String image;
        public String text;

        public Hint() {}

        public Hint(@Nullable String image, @Nullable String text) {
            this.image = image;
            this.text = text;
        }

        protected Hint(Parcel in) {
            image = in.readString();
            text = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(image);
            dest.writeString(text);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<Hint> CREATOR = new Creator<Hint>() {
            @Override
            public Hint createFromParcel(Parcel in) {
                return new Hint(in);
            }

            @Override
            public Hint[] newArray(int size) {
                return new Hint[size];
            }
        };
    }

    public static class Option implements Parcelable {

        public String text;
        public String explanation;

        public Option() {}

        public Option(@NonNull String text, @Nullable String explanation) {
            this.text = text;
            this.explanation = explanation;
        }

        protected Option(Parcel in) {
            text = in.readString();
            explanation = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(text);
            dest.writeString(explanation);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<Option> CREATOR = new Creator<Option>() {
            @Override
            public Option createFromParcel(Parcel in) {
                return new Option(in);
            }

            @Override
            public Option[] newArray(int size) {
                return new Option[size];
            }
        };
    }

}
