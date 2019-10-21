![csplogo](https://user-images.githubusercontent.com/12301571/67168219-4d618900-f3a2-11e9-9460-b79eff997c35.PNG)
# cmd.csp.classifier
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Maintenance](https://img.shields.io/badge/Maintained%3F-yes-green.svg)](https://GitHub.com/swelcker/cmd.csp.classifier/graphs/commit-activity)
[![GitHub release](https://img.shields.io/github/release/swelcker/cmd.csp.classifier.svg)](https://GitHub.com/swelcker/cmd.csp.classifier/releases/)
[![GitHub tag](https://img.shields.io/github/tag/swelcker/cmd.csp.classifier.svg)](https://GitHub.com/swelcker/cmd.csp.classifier/tags/)
[![GitHub commits](https://img.shields.io/github/commits-since/swelcker/cmd.csp.classifier/master.svg)](https://GitHub.com/swelcker/cmd.csp.classifier/commit/)
[![GitHub contributors](https://img.shields.io/github/contributors/swelcker/cmd.csp.classifier.svg)](https://GitHub.com/swelcker/cmd.csp.classifier/graphs/contributors/)


Simple implementation of text classifier in Java with built in SVM, C4.5, kNN, and naive Bayesian classifiers.
Support for common text preprocessors and for CVS format. You can plugin your own classifier, tokenizer, transformer, stopwords, synonyms, and TF-IDF formula etc.
Supports automatic validation and confusion matrix. Used in the Cognitive Service Platform cmd.csp as part of the classifier features.

### Classifier

Classifiers are used to assign class labels to token streams. The toolkit includes:

- kNN classifier. this classifier searches for k samples nearest to a token stream,
  and then label the stream using the labels of the samples. Since iterate over all
  samples is needed, it may be very slow for large datasets.
- Naive Bayesian classifier. This classifier estimate the probability that the stream
  belong to a class, assuming the appearance of tokens is independent.
- TF-IDF classifier. This classifier calculate the angle between the token TF-IDF
  vector of the stream and the token TF-IDF vector of the class.
- SVM (libSVM/liblinear) classifier. This classifier use support vector machine which solve a kind
  of conditional optimization problem. This is the preferred classifier for text in cmd.csp.
- C4.5 classifier. This classifier use decision trees to classify objects.

### Prerequisites

There are no prerequisites. 
Included dependencies others than java core:
```xml
<dependency>
    <groupId>com.ibm.icu</groupId>
    <artifactId>icu4j</artifactId>
    <version>64.2</version>
</dependency>
<dependency>
    <groupId>de.bwaldvogel</groupId>
    <artifactId>liblinear</artifactId>
    <version>2.30</version>
</dependency>
<dependency>
    <groupId>cmd.csp</groupId>
    <artifactId>cspstemmer</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Installing/Usage

1. To use, merge the following into your Maven POM (or the equivalent into your Gradle build script):

```xml
<repository>
  <id>github</id>
  <name>GitHub swelcker Apache Maven Packages</name>
  <url>https://maven.pkg.github.com/swelcker</url>
</repository>

<dependency>
  <groupId>cmd.csp</groupId>
  <artifactId>cspclassifier</artifactId>
  <version>1.0.0</version>
</dependency>
```

2. Text need to be tokenizied before being used to train or classify. The toolkit includes:
- Java class `java.text.BreakIterator` based locale awared method, recommended if
  the locale of the text is supported by Java.
- Split using a regular expression that matches separator, the other parts can be kept or not
  according to your opinion.
- Split using a regular expression that matches token, the other parts can be kept or not
  according to your opinion.

3. Filters transform a token stream into another. The toolkit includes:
   - Used to normalize text，
       - Normalize Unicode
       - Apply transformations provided by icu4j
       - Upcase
       - Downcase
       - Fold case. Since there are no one to one corresponding between lower case letters
         and upper case letters in many languages, case folding should be used to ignore case.
       - Stemming，i.e. convert words into their root from. Stemming algorithm from Snowball
         are included:  Arabic,  Danish,  Dutch,  English,  Finnish,  French,  German,
         Hungarian,  Indonesian,  Irish,  Italian,  Nepali,  Norwegian,  Portuguese,
         Romanian,  Spanish,  Russian,  Swedish,  Tamil,  Turkish
       - Text replacement based on regular expression(Backward reference is allowed)
       - User-defined mapping
   - Remove some tokens from the stream, e.g.
       - Remove token that are whitespace
       - Remove stop words
       - Keep only protected words
       - Remove tokens that match a regular expression
       - Remove tokens that do not match a regular expression
   - Map a token into zero or more tokens, e.g.
       - Insert synonyms
       - User-defined mapping
   - Convert the stream into a stream form by n-gram from the original stream

Then, import cspclassifier.*;` in your application :

```java
// Example
import cspclassifier.*;
import java.io.*;
import java.util.*;
...
protected ClassifierFactory classifierFactory;
protected Trainable<String> model;
protected Classifier<String> classifier;

protected Category cat= null;
protected Map<String, Category> catList = new HashMap<String, Category>();
protected Locale locl=Locale.getDefault();
...

classifierFactory=Starter.getDefaultClassifierFactory(locl);
model= classifierFactory.createModel();

...

//create the categorys and maybe store them in a list, so you can reause them
cat = new Category(strCategory);
catList.put(strCategory, cat);

...

// train the model
classifier=classifierFactory.getClassifier(model);

...
// finally classify a new text to get results
List<ClassificationResult> res = classifier.getCandidates(currentText, maxCategories);


```

## Built With

* [Maven](https://maven.apache.org/) - Dependency Management


## Contributing

Please read [CONTRIBUTING.md](https://gist.github.com/PurpleBooth/b24679402957c63ec426) for details on our code of conduct, and the process for submitting pull requests to us.

## Versioning

We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/swelcker/cmd.csp.classifier/tags). 

## Authors

* **Stefan Welcker** - *Modifications based on chungkwong/text-classifier-collection* 

See also the list of [contributors](https://github.com/swelcker/cmd.csp.classifier/contributors) who participated in this project.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details


