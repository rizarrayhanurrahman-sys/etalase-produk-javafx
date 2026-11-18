package controller;

import java.net.URL;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Optional;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import data.ProdukRepository;
import model.Produk;

public class KatalogController {

    private static final String TEKS_SIMPAN = "Simpan Produk";
    private static final String TEKS_UBAH = "Simpan Perubahan";

    @FXML
    private TextField namaField;
    @FXML
    private TextField hargaField;
    @FXML
    private ComboBox<String> fotoBox;
    @FXML
    private Label pesanLabel;
    @FXML
    private Label jumlahLabel;
    @FXML
    private Label modeLabel;
    @FXML
    private Button simpanButton;
    @FXML
    private Button batalButton;
    @FXML
    private TilePane katalogPane;
    private final ProdukRepository repository = new ProdukRepository();
    private final NumberFormat formatRupiah = NumberFormat.getInstance(Locale.forLanguageTag("id-ID"));
    private Produk produkDiedit;
    private VBox kartuAktif;

    @FXML
    private void initialize() {
        fotoBox.getItems().add("roti.png");
        fotoBox.getItems().add("susu.png");
        fotoBox.getItems().add("kopi.png");
        kembaliKeModeTambah();
        try {
            showData();
        } catch (SQLException e) {
            pesanLabel.setText("Gagal membaca database. Lihat Output.");
            e.printStackTrace();
        }
    }

    private void showData() throws SQLException {
        ArrayList<Produk> daftar = repository.getAll();
        katalogPane.getChildren().clear();
        for (Produk produk : daftar) {
            VBox kartu = createProductCard(produk);
            katalogPane.getChildren().add(kartu);
        }
        jumlahLabel.setText("Jumlah produk: " + daftar.size());
    }

    private VBox createProductCard(Produk produk) {
        ImageView fotoView = new ImageView();
        fotoView.setFitWidth(170);
        fotoView.setFitHeight(110);
        fotoView.setPreserveRatio(true);
        StackPane bingkai = new StackPane();
        bingkai.setPrefSize(180, 120);
        bingkai.setMinSize(180, 120);
        bingkai.setMaxSize(180, 120);

        URL lokasi = getClass().getResource("/image/" + produk.getFoto());
        if (lokasi != null) {
            Image gambar = new Image(lokasi.toExternalForm(),
                    170, 110, true, true);
            if (!gambar.isError()) {
                fotoView.setImage(gambar);
                bingkai.getChildren().add(fotoView);
            } else {
                bingkai.getChildren().add(new Label("Gambar tidak terbaca"));
            }
        } else {
            bingkai.getChildren().add(new Label("Gambar tidak tersedia"));
        }

        Label namaLabel = new Label(produk.getNama());
        namaLabel.setWrapText(true);
        namaLabel.getStyleClass().add("nama-produk");
        Label hargaLabel = new Label("Rp " + formatRupiah.format(produk.getHarga()));
        Label idLabel = new Label("ID: " + produk.getId());
        idLabel.getStyleClass().add("muted");

        VBox kartu = new VBox();

        Button ubahButton = new Button("Ubah");
        ubahButton.getStyleClass().add("tombol-kartu-ubah");
        ubahButton.setOnAction(e -> mulaiUbah(produk, kartu));
        Button hapusButton = new Button("Hapus");
        hapusButton.getStyleClass().add("tombol-kartu-hapus");
        hapusButton.setOnAction(e -> hapusKartu(produk));
        HBox aksi = new HBox(8);
        aksi.getChildren().add(ubahButton);
        aksi.getChildren().add(hapusButton);

        kartu.setSpacing(8);
        kartu.setMinWidth(220);
        kartu.setPrefWidth(220);
        kartu.setMaxWidth(220);
        kartu.getStyleClass().add("kartu-produk");
        kartu.getChildren().add(bingkai);
        kartu.getChildren().add(namaLabel);
        kartu.getChildren().add(hargaLabel);
        kartu.getChildren().add(idLabel);
        kartu.getChildren().add(aksi);
        return kartu;
    }

    @FXML
    private void saveProduct() {
        boolean modeUbah = produkDiedit != null;
        Produk produk;
        try {
            String nama = namaField.getText();
            long harga = Long.parseLong(hargaField.getText().trim());
            String foto = fotoBox.getValue();
            if (modeUbah) {
                produk = new Produk(produkDiedit.getId(), nama, harga, foto);
            } else {
                produk = new Produk(nama, harga, foto);
            }
        } catch (NumberFormatException e) {
            pesanLabel.setText("Harga harus angka bulat, contoh 12000.");
            return;
        } catch (IllegalArgumentException e) {
            pesanLabel.setText(e.getMessage());
            return;
        }
        try {
            if (modeUbah) {
                repository.ubah(produk);
            } else {
                repository.add(produk);
            }
        } catch (SQLException e) {
            pesanLabel.setText("Gagal menyimpan. Lihat Output.");
            e.printStackTrace();
            return;
        }
        kembaliKeModeTambah();
        try {
            showData();
            if (modeUbah) {
                pesanLabel.setText("Produk ID " + produk.getId() + " berhasil diubah.");
            } else {
                pesanLabel.setText("Produk berhasil disimpan.");
            }
        } catch (SQLException e) {
            pesanLabel.setText("Data tersimpan, tetapi katalog gagal dimuat ulang.");
            e.printStackTrace();
        }
    }

    @FXML
    private void batalUbah() {
        kembaliKeModeTambah();
        pesanLabel.setText("Perubahan dibatalkan.");
    }

    private void mulaiUbah(Produk produk, VBox kartu) {
        if (kartuAktif != null) {
            kartuAktif.getStyleClass().remove("kartu-aktif");
        }
        kartuAktif = kartu;
        kartu.getStyleClass().add("kartu-aktif");
        produkDiedit = produk;
        namaField.setText(produk.getNama());
        hargaField.setText(String.valueOf(produk.getHarga()));
        fotoBox.setValue(produk.getFoto());
        simpanButton.setText(TEKS_UBAH);
        batalButton.setVisible(true);
        batalButton.setManaged(true);
        modeLabel.setText("Mode ubah: produk ID " + produk.getId());
        pesanLabel.setText("");
    }

    private void kembaliKeModeTambah() {
        if (kartuAktif != null) {
            kartuAktif.getStyleClass().remove("kartu-aktif");
        }
        kartuAktif = null;
        produkDiedit = null;
        namaField.clear();
        hargaField.clear();
        fotoBox.setValue("roti.png");
        simpanButton.setText(TEKS_SIMPAN);
        batalButton.setVisible(false);
        batalButton.setManaged(false);
        modeLabel.setText("Mode tambah: produk baru");
    }

    private void hapusKartu(Produk produk) {
        ButtonType ya = new ButtonType("Ya, hapus", ButtonBar.ButtonData.OK_DONE);
        ButtonType tidak = new ButtonType("Batal", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert konfirmasi = new Alert(Alert.AlertType.WARNING,
                "Produk \"" + produk.getNama() + "\" akan dihapus permanen.",
                ya, tidak);
        konfirmasi.setTitle("Konfirmasi hapus");
        konfirmasi.setHeaderText("Hapus produk ini?");
        Optional<ButtonType> jawaban = konfirmasi.showAndWait();
        if (jawaban.isEmpty() || jawaban.get() != ya) {
            return;
        }
        try {
            repository.hapus(produk.getId());
        } catch (SQLException e) {
            pesanLabel.setText("Gagal menghapus. Lihat Output.");
            e.printStackTrace();
            return;
        }
        if (produkDiedit != null && produkDiedit.getId() == produk.getId()) {
            kembaliKeModeTambah();
        }
        try {
            showData();
            pesanLabel.setText("Produk \"" + produk.getNama() + "\" dihapus.");
        } catch (SQLException e) {
            pesanLabel.setText("Data terhapus, tetapi katalog gagal dimuat ulang.");
            e.printStackTrace();
        }
    }
}