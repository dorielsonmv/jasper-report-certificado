package tech.jamersondev.jasperreports.service;

import net.sf.jasperreports.engine.*;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import tech.jamersondev.jasperreports.model.Aluno;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Service
public class JasperReportService {

    public static final String CERTIFICADOS = "classpath:jasper/certificados/";
    public static final String IMAGEBG = "classpath:jasper/img/jasper-img.png";
    public static final String ARQUIVOJRXML = "cert.jrxml";
    public static final Logger LOGGER = LoggerFactory.getLogger(JasperReportService.class);

    // INÍCIO DA MODIFICAÇÃO
    // 1. Obtém o diretório Home do usuário
    public static final String HOME_DIR = System.getProperty("user.home");

    // 2. Define o caminho completo: /home/usuario/jasper-report
    public static final String DESTINOPDF = HOME_DIR + File.separator + "jasper-report";
    // FIM DA MODIFICAÇÃO


    public void gerar(Aluno aluno) throws IOException {

        byte[] imagebg = this.loadimage(IMAGEBG);

        Map<String, Object> params = new HashMap<>();
        params.put("nome", aluno.getNome());
        params.put("curso", aluno.getCurso());
        params.put("cargaHoraria", aluno.getCargaHoraria());
        params.put("dataInicioCurso", aluno.getDataInicioCurso());
        params.put("dataTerminoCurso", aluno.getDataTerminoCurso());
        params.put("imageJasper", imagebg);

        String pathAbsoluto = getAbsultePath();
        try{
            // Gera um nome de arquivo único
            String nomeArquivo = "certificado-" + aluno.getNome().replaceAll("\\s+", "_") + ".pdf";
            String folderDiretorio = getDiretorioSave(nomeArquivo);

            JasperReport report = JasperCompileManager.compileReport(pathAbsoluto);
            LOGGER.info("report compilado: {} ", pathAbsoluto);
            JasperPrint print = JasperFillManager.fillReport(report, params, new JREmptyDataSource());
            LOGGER.info("jasper print");
            JasperExportManager.exportReportToPdfFile(print, folderDiretorio);
            // O log agora mostrará o caminho completo, ex: /home/usuario/jasper-report/certificado-Nome.pdf
            LOGGER.info("PDF EXPORTADO PARA: {}", folderDiretorio);

        } catch (JRException e) {
            throw new RuntimeException(e);
        }

    }

    private byte[] loadimage(String imagebg) throws IOException {
        String image = ResourceUtils.getFile(imagebg).getAbsolutePath();
        File file = new File(image);
        try(InputStream inputStream = new FileInputStream(file)){
            return IOUtils.toByteArray(inputStream);
        }
    }

    private String getDiretorioSave(String name) {
        this.createDiretorio(); // Chama o método para criar o diretório base
        return DESTINOPDF + File.separator + name;
    }

    // MÉTODO createDiretorio ATUALIZADO
    private void createDiretorio() {
        File dir = new File(DESTINOPDF); // Usa o caminho completo do Home Dir
        if(!dir.exists()){
            // Usa mkdirs() para garantir que todos os diretórios no caminho sejam criados
            dir.mkdirs();
        }
    }

    private String getAbsultePath() throws FileNotFoundException {
        return ResourceUtils.getFile(CERTIFICADOS+ARQUIVOJRXML).getAbsolutePath();
    }
}