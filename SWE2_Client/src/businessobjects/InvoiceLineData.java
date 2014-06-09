package businessobjects;

public class InvoiceLineData {
	private int idNumber = -1;
	private int menge;
	private double stueckPreis;
	private double brutto;
	private double netto;
	private String articleName;
	private Article article;
	private double MWSt;
	
	public int getIdNumber() {
		return idNumber;
	}
	public void setIdNumber(int idNumber) {
		this.idNumber = idNumber;
	}
	public int getMenge() {
		return menge;
	}
	public void setMenge(int menge) {
		this.menge = menge;
	}
	public double getStueckPreis() {
		return stueckPreis;
	}
	public void setStueckPreis(double stueckPreis) {
		this.stueckPreis = stueckPreis;
	}
	public double getBrutto() {
		return brutto;
	}
	public void setBrutto(double brutto) {
		this.brutto = brutto;
	}
	public double getNetto() {
		return netto;
	}
	public void setNetto(double netto) {
		this.netto = netto;
	}
	public String getArticleName() {
		return articleName;
	}
	public void setArticleName(String articleName) {
		this.articleName = articleName;
	}
	public Article getArticle() {
		return article;
	}
	public void setArticle(Article article) {
		this.article = article;
	}
	public double getMWSt() {
		return MWSt;
	}
	public void setMWSt(double mWSt) {
		MWSt = mWSt;
	}
	
}
