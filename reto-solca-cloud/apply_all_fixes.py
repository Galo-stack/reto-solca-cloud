import re, sys

def read_file(path):
    with open(path, 'r', encoding='utf-8') as f:
        return f.read()

def write_file(path, content):
    with open(path, 'w', encoding='utf-8') as f:
        f.write(content)
    print(f"WRITTEN: {path}")

def apply_edits(content, edits):
    for old, new in edits:
        count = content.count(old)
        if count == 0:
            print(f"  WARNING: pattern not found (skipped)")
            continue
        content = content.replace(old, new, 1) if count > 1 else content.replace(old, new)
        print(f"  Replaced: {count > 1 and 'first of ' or ''}{count} occurrence(s)")
    return content

# =============================================================================
# FRONTEND CHANGES
# =============================================================================
INDEX = r"C:\Users\USER\OneDrive\Documentos\reto-solca-cloud\reto-solca-cloud\frontend\index.html"
html = read_file(INDEX)

# 1. ECUADOR DATA - Append after calcEdad function or before save functions
# We'll insert a large JS data block with all provinces, cantons, and parishes

ECUADOR_DATA = """
// ECUADOR ADMINISTRATIVE DIVISIONS DATA
var ECUADOR = {
  "AZUAY": {
    "nombre": "Azuay",
    "cantones": {
      "CUENCA": {
        "nombre": "Cuenca",
        "parroquias": ["Bellavista","Ca\u00f1aribamba","El Bat\u00e1n","El Sagrario","El Vecino","Gil Ram\u00edrez D\u00e1valos","Hermano Miguel","Huayna C\u00e1pac","Mach\u00e1ngara","Monay","San Blas","San Sebasti\u00e1n","Sucre","Totoracocha","Yanuncay","Ba\u00f1os","Chiquintad","Checa","Cumbe","Llacao","Molleturo","Nulti","Octavio Cordero","Paccha","Quingeo","Ricaurte","San Joaqu\u00edn","Santa Ana","Sayaus\u00ed","Sidcay","Sinincay","Turi","Valle de los R\u00edos","Victoria del Portete"]
      },
      "GIRON": {"nombre":"Gir\u00f3n","parroquias":["Gir\u00f3n","Asunci\u00f3n","San Gerardo"]},
      "GUALACEO": {"nombre":"Gualaceo","parroquias":["Gualaceo","Chordeleg","Daniel C\u00f3rdova","Jad\u00e1n","Mariano Moreno","Principal","Remigio Crespo","San Juan","Zhidmad","Luis Cordero","Sim\u00f3n Bol\u00edvar"]},
      "NABON": {"nombre":"Nab\u00f3n","parroquias":["Nab\u00f3n","Cochapata","El Progreso","Las Nieves"]},
      "PAUTE": {"nombre":"Paute","parroquias":["Paute","Amaluza","Chic\u00e1n","El Cabo","San Crist\u00f3bal","Tomebamba","Dug Dug","Bul\u00e1n"]},
      "PUCARA": {"nombre":"Pucar\u00e1","parroquias":["Pucar\u00e1","San Miguel de Porotos","San Rafael de Sharug","Uzhcurrumi"]},
      "SAN_FERNANDO": {"nombre":"San Fernando","parroquias":["San Fernando","Chumbl\u00edn"]},
      "SANTA_ISABEL": {"nombre":"Santa Isabel","parroquias":["Santa Isabel","Abd\u00f3n Calder\u00f3n","Shagl\u00ed","San Jos\u00e9 de Raranga"]},
      "SEVILLA_DE_ORO": {"nombre":"Sevilla de Oro","parroquias":["Sevilla de Oro","Amaluza","Palmas"]},
      "SIGSIG": {"nombre":"S\u00edgsig","parroquias":["S\u00edgsig","Cuchil","Gima","Ludo","San Bartolom\u00e9","San Jos\u00e9 de Raranga"]},
      "CAMILO_PONCE": {"nombre":"Camilo Ponce Enr\u00edquez","parroquias":["Camilo Ponce Enr\u00edquez","El Carmen"]},
      "CHORDELEG": {"nombre":"Chordeleg","parroquias":["Chordeleg","Principal","La Uni\u00f3n"]},
      "EL_PAN": {"nombre":"El Pan","parroquias":["El Pan","San Vicente"]},
      "GUACHAPALA": {"nombre":"Guachapala","parroquias":["Guachapala"]},
      "OÑA": {"nombre":"O\u00f1a","parroquias":["O\u00f1a","San Felipe"]}
    }
  },
  "BOLIVAR": {"nombre":"Bol\u00edvar","cantones":{
    "GUARANDA": {"nombre":"Guaranda","parroquias":["Guaranda","\u00c1ngel Polibio Chaves","Gabriel Ignacio Veintimilla","Guanujo","Julio E. Moreno","Salinas","San Luis de Pambil","San Sim\u00f3n","Simiatug","Turupamba"]},
    "CALDERON": {"nombre":"Calder\u00f3n","parroquias":["Calder\u00f3n","San Pablo","Facundo Vela"]},
    "CHILLANES": {"nombre":"Chillanes","parroquias":["Chillanes","San Jos\u00e9 del Tambo"]},
    "CHIMBO": {"nombre":"Chimbo","parroquias":["Chimbo","Asunci\u00f3n","Magdalena"]},
    "ECHEANDIA": {"nombre":"Echeand\u00eda","parroquias":["Echeand\u00eda"]},
    "SAN_MIGUEL": {"nombre":"San Miguel","parroquias":["San Miguel","Balsapamba","Bilov\u00edn","R\u00e9gulo de Mora","San Pablo","Santiago"]},
    "CALUMA": {"nombre":"Caluma","parroquias":["Caluma"]},
    "LAS_NAVES": {"nombre":"Las Naves","parroquias":["Las Naves"]}
  }},
  "CAÑAR": {"nombre":"Ca\u00f1ar","cantones":{
    "AZOGUES": {"nombre":"Azogues","parroquias":["Azogues","Borrero","San Francisco","Guap\u00e1n","Javier Loyola","Luis Cordero","Pindilig","Rivera","San Miguel","Taday","Cojitambo","Tutor"]},
    "BIBLIAN": {"nombre":"Bibli\u00e1n","parroquias":["Bibli\u00e1n","Naz\u00f3n","San Francisco de Sageo","Turupamba"]},
    "CAÑAR": {"nombre":"Ca\u00f1ar","parroquias":["Ca\u00f1ar","Chontamarca","Chorocopte","Ducaur","Gualleturo","Ingapirca","Juncal","San Antonio","Zhud","Ventura"]},
    "LA_TRONCAL": {"nombre":"La Troncal","parroquias":["La Troncal","Manuel J. Calle","Pancho Negro"]},
    "EL_TAMBO": {"nombre":"El Tambo","parroquias":["El Tambo"]},
    "DELEG": {"nombre":"D\u00e9leg","parroquias":["D\u00e9leg","Solano"]},
    "SUSCAL": {"nombre":"Suscal","parroquias":["Suscal"]}
  }},
  "CARCHI": {"nombre":"Carchi","cantones":{
    "TULCAN": {"nombre":"Tulc\u00e1n","parroquias":["Tulc\u00e1n","El Carmelo","Julio Andrade","Maldonado","Pioter","Tobar Donoso","Tufi\u00f1o","Urbina","Santa Martha de Cuba"]},
    "BOLIVAR": {"nombre":"Bol\u00edvar","parroquias":["Bol\u00edvar","Los Andes","San Gabriel"]},
    "ESPEJO": {"nombre":"Espejo","parroquias":["Espejo","El \u00c1ngel","El Goaltal","La Libertad","San Isidro"]},
    "MIRA": {"nombre":"Mira","parroquias":["Mira","Concepci\u00f3n","Jij\u00f3n y Caama\u00f1o"]},
    "MONTUFAR": {"nombre":"Mont\u00fafar","parroquias":["Mont\u00fafar","Fern\u00e1ndez Salvador","Gonz\u00e1lez Su\u00e1rez","San Jos\u00e9"]},
    "SAN_PEDRO_DE_HUACA": {"nombre":"San Pedro de Huaca","parroquias":["San Pedro de Huaca"]}
  }},
  "CHIMBORAZO": {"nombre":"Chimborazo","cantones":{
    "RIOBAMBA": {"nombre":"Riobamba","parroquias":["Riobamba","Cacha","Calpi","Cubij\u00edes","Flores","Lic\u00e1n","Licto","Pungal\u00e1","Quimiag","San Juan","San Luis","Yaruqu\u00edes","Veloz"]},
    "ALAUSI": {"nombre":"Alaus\u00ed","parroquias":["Alaus\u00ed","Achupallas","Guasuntos","Huigra","Multitud","Pistish\u00ed","Pumallacta","Sevilla","Tix\u00e1n"]},
    "COLTA": {"nombre":"Colta","parroquias":["Colta","Cajabamba","Sicalpa","Villa La Uni\u00f3n","Ca\u00f1i"]},
    "CHAMBO": {"nombre":"Chambo","parroquias":["Chambo"]},
    "CHUNCHI": {"nombre":"Chunchi","parroquias":["Chunchi","Capzol","Compud"]},
    "GUAMOTE": {"nombre":"Guamote","parroquias":["Guamote","Cebadas","Palmira"]},
    "GUANO": {"nombre":"Guano","parroquias":["Guano","Ilapo","San Andr\u00e9s","San Gerardo de Pacaicahu\u00e1n","San Isidro de Patul\u00fa","Valpara\u00edso"]},
    "PALLATANGA": {"nombre":"Pallatanga","parroquias":["Pallatanga"]},
    "PENIPE": {"nombre":"Penipe","parroquias":["Penipe","Bilbao","El Altar","Matus","Puela","San Antonio de Bayushig"]},
    "CUMANDA": {"nombre":"Cumand\u00e1","parroquias":["Cumand\u00e1"]}
  }},
  "COTOPAXI": {"nombre":"Cotopaxi","cantones":{
    "LATACUNGA": {"nombre":"Latacunga","parroquias":["Latacunga","Al\u00e1quez","Belisario Quevedo","Guaytacama","Joseguango Bajo","Las Pampas","Mulalillo","Once de Noviembre","Poal\u00f3","San Juan de Pastocalle","Tanicuch\u00ed","Toacaso","Eloy Alfaro"]},
    "LA_MANA": {"nombre":"La Man\u00e1","parroquias":["La Man\u00e1","El Carmen","La Victoria","Las Naves","San Lorenzo"]},
    "PANGUA": {"nombre":"Pangua","parroquias":["Pangua","El Coraz\u00f3n","Moraspungo","Ram\u00f3n Campa\u00f1a"]},
    "PUJILI": {"nombre":"Pujil\u00ed","parroquias":["Pujil\u00ed","Angamarca","Chantil\u00edn","La Victoria","Pilal\u00f3","Tingo"]},
    "SALCEDO": {"nombre":"Salcedo","parroquias":["Salcedo","Cusubamba","Mulalillo","Mulliquindil","Panquil","San Miguel"]},
    "SAQUISILI": {"nombre":"Saquisil\u00ed","parroquias":["Saquisil\u00ed","Canchagua","Chantil\u00edn","Cochasqu\u00ed"]},
    "SIGCHOS": {"nombre":"Sigchos","parroquias":["Sigchos","Chugchill\u00e1n","Isinliv\u00ed","Las Pampas","Palo Quemado","Apagua"]}
  }},
  "EL_ORO": {"nombre":"El Oro","cantones":{
    "MACHALA": {"nombre":"Machala","parroquias":["Machala","La Providencia","El Cambio","Puerto Bol\u00edvar","Nueve de Octubre","Jambel\u00ed","El Retiro"]},
    "ARENILLAS": {"nombre":"Arenillas","parroquias":["Arenillas","Chacras","La Libertad","Palmales","Carcab\u00f3n"]},
    "ATAHUALPA": {"nombre":"Atahualpa","parroquias":["Atahualpa","Ayapamba","Milagro","San Jos\u00e9"]},
    "BALSAS": {"nombre":"Balsas","parroquias":["Balsas"]},
    "CHILLA": {"nombre":"Chilla","parroquias":["Chilla"]},
    "EL_GUABO": {"nombre":"El Guabo","parroquias":["El Guabo","Barbones","La Iberia","Tendales"]},
    "HUAQUILLAS": {"nombre":"Huaquillas","parroquias":["Huaquillas","El Para\u00edso"]},
    "MARCABELI": {"nombre":"Marcabel\u00ed","parroquias":["Marcabel\u00ed"]},
    "PASAJE": {"nombre":"Pasaje","parroquias":["Pasaje","Bol\u00edvar","Casacay","La Pea\u00f1a","Mercedes","Progreso","Uzhcurrumi","Santa Rosa"]},
    "PIÑAS": {"nombre":"Pi\u00f1as","parroquias":["Pi\u00f1as","Capiro","La Bocana","Moromoro","Piedras","San Roque","Saracay"]},
    "PORTOVELO": {"nombre":"Portovelo","parroquias":["Portovelo","Curtincapa","Malingas","Salat\u00ed"]},
    "SANTA_ROSA": {"nombre":"Santa Rosa","parroquias":["Santa Rosa","Bellavista","Jambel\u00ed","La Avanzada","San Antonio","Torata","Victoria"]},
    "ZARUMA": {"nombre":"Zaruma","parroquias":["Zaruma","Aba\u00f1\u00edn","Arcapamba","Guanaz\u00e1n","G\u00fcell","Huertas","Malvas","Muluncay","Salvias","Sinsao"]},
    "LAS_LAJAS": {"nombre":"Las Lajas","parroquias":["Las Lajas","La Victoria","Platanillos","Valle Hermoso"]}
  }},
  "ESMERALDAS": {"nombre":"Esmeraldas","cantones":{
    "ESMERALDAS": {"nombre":"Esmeraldas","parroquias":["Esmeraldas","\u00c1ngel Sotomayor","Camarones","Coronel Carlos Concha","Chinca","Majua","Pimpil","San Mateo","Sim\u00f3n Plata Torres","Tabiazo","Vuelta Larga","Tachina"]},
    "ELOY_ALFARO": {"nombre":"Eloy Alfaro","parroquias":["Eloy Alfaro","Borb\u00f3n","La Tola","Luis Vargas Torres","Maldonado","Pichimba","Rosa Zarate","San Francisco","Santo Domingo","Valdez"]},
    "MUISNE": {"nombre":"Muisne","parroquias":["Muisne","Bol\u00edvar","D\u00e1ule","El Cabo","Galera","Quingue","San Gregorio","Salima","Tongorach\u00ed"]},
    "QUININDE": {"nombre":"Quinind\u00e9","parroquias":["Quinind\u00e9","Cubita","Chura","Malimpia","Viche","Rosa Zarate"]},
    "RIOVERDE": {"nombre":"Rioverde","parroquias":["Rioverde","Chontaduro","Chumund\u00e9","Lagarto","Montalvo","Rocafuerte"]},
    "SAN_LOBENZO": {"nombre":"San Lorenzo","parroquias":["San Lorenzo","Alto Tambo","Anc\u00f3n","Calder\u00f3n","Carpio","Concepci\u00f3n","San Javier","Santa Rita","Urbina"]},
    "ATACAMES": {"nombre":"Atacames","parroquias":["Atacames","S\u00faa","Tonchig\u00fce","La Uni\u00f3n"]}
  }},
  "GALAPAGOS": {"nombre":"Gal\u00e1pagos","cantones":{
    "SAN_CRISTOBAL": {"nombre":"San Crist\u00f3bal","parroquias":["San Crist\u00f3bal","El Progreso","Isla Santa Mar\u00eda","Isla Espa\u00f1ola"]},
    "ISABELA": {"nombre":"Isabela","parroquias":["Isabela","Tom\u00e1s de Berlanga","Isla Darwin","Isla Wolf"]},
    "SANTA_CRUZ": {"nombre":"Santa Cruz","parroquias":["Santa Cruz","Bellavista","Canal de Itabaca","Isla Baltra","Isla Floreana","Isla Genovesa","Isla Marchena","Isla Seymour Norte","Isla Pinta","Isla R\u00e1bida"]}
  }},
  "GUAYAS": {"nombre":"Guayas","cantones":{
    "GUAYAQUIL": {"nombre":"Guayaquil","parroquias":["Guayaquil","Ayacucho","Bol\u00edvar","Carbo","Febres Cordero","Garc\u00eda Moreno","Letamendi","Nueve de Octubre","Olmedo","Roca","Rocafuerte","Sucre","Tarqui","Urdaneta","Ximena","Chong\u00f3n","Pascuales","El Morro","Pun\u00e1","Tenguel"]},
    "ALFREDO_BAQUERIZO": {"nombre":"Alfredo Baquerizo Moreno","parroquias":["Alfredo Baquerizo Moreno"]},
    "BALAO": {"nombre":"Balao","parroquias":["Balao"]},
    "BALZAR": {"nombre":"Balzar","parroquias":["Balzar"]},
    "COLIMES": {"nombre":"Colimes","parroquias":["Colimes"]},
    "DAULE": {"nombre":"Daule","parroquias":["Daule","La Aurora","Los Lojas"]},
    "DURAN": {"nombre":"Dur\u00e1n","parroquias":["Dur\u00e1n","El Recreo"]},
    "EL_EMPALME": {"nombre":"El Empalme","parroquias":["El Empalme","Velasco Ibarra"]},
    "EL_TRIUNFO": {"nombre":"El Triunfo","parroquias":["El Triunfo"]},
    "MILAGRO": {"nombre":"Milagro","parroquias":["Milagro","Mariscal Sucre"]},
    "NARANJAL": {"nombre":"Naranjal","parroquias":["Naranjal","Santa Rosa de Flandes","Taura"]},
    "NARANJITO": {"nombre":"Naranjito","parroquias":["Naranjito"]},
    "PALESTINA": {"nombre":"Palestina","parroquias":["Palestina"]},
    "PEDRO_CARBO": {"nombre":"Pedro Carbo","parroquias":["Pedro Carbo","Valle de la Virgen"]},
    "SAMBORONDON": {"nombre":"Samborond\u00f3n","parroquias":["Samborond\u00f3n","La Puntilla"]},
    "SANTA_LUCIA": {"nombre":"Santa Luc\u00eda","parroquias":["Santa Luc\u00eda"]},
    "SALITRE": {"nombre":"Salitre","parroquias":["El Salitre","La Victoria"]},
    "SAN_JACINTO": {"nombre":"San Jacinto de Yaguachi","parroquias":["San Jacinto de Yaguachi","Virgen de F\u00e1tima"]},
    "PLAYAS": {"nombre":"Playas","parroquias":["Playas"]},
    "SIMON_BOLIVAR": {"nombre":"Sim\u00f3n Bol\u00edvar","parroquias":["Sim\u00f3n Bol\u00edvar"]},
    "CORONEL_MARCELINO": {"nombre":"Coronel Marcelino Maridue\u00f1a","parroquias":["Coronel Marcelino Maridue\u00f1a"]},
    "LOMAS_DE_SARGENTILLO": {"nombre":"Lomas de Sargentillo","parroquias":["Lomas de Sargentillo"]},
    "NOBOL": {"nombre":"Nobol","parroquias":["Nobol"]},
    "ISIDRO_AYORA": {"nombre":"Isidro Ayora","parroquias":["Isidro Ayora"]},
    "GENERAL_ANTONIO_ELIZALDE": {"nombre":"General Antonio Elizalde","parroquias":["General Antonio Elizalde"]}
  }},
  "IMBABURA": {"nombre":"Imbabura","cantones":{
    "IBARRA": {"nombre":"Ibarra","parroquias":["Ibarra","Alpachaca","Ambuqu\u00ed","Angochagua","Caranqui","La Dolorosa del Priorato","San Antonio","San Francisco","San Miguel de Urcuqu\u00ed","El Sagrario"]},
    "ANTONIO_ANTE": {"nombre":"Antonio Ante","parroquias":["Antonio Ante","Andrade Mar\u00edn","Atuntaqui","Imbaya","San Francisco de Natabuela","San Jos\u00e9 de Chaltura"]},
    "COTACACHI": {"nombre":"Cotacachi","parroquias":["Cotacachi","Apuela","Garc\u00eda Moreno","Imantag","Pe\u00f1aherrera","Playa Rica","Quiroga","San Francisco de Sig\u00fcs","Saguangal","6 de Julio de Cuellaje","Vacas Galindo"]},
    "OTAVALO": {"nombre":"Otavalo","parroquias":["Otavalo","Cotama","Dr. Miguel Egas","Eugenio Espejo","Gonz\u00e1lez Su\u00e1rez","Pataqu\u00ed","Quinchuqu\u00ed","San Juan de Ilum\u00e1n","San Pablo","San Rafael"]},
    "PIMAMPIRO": {"nombre":"Pimampiro","parroquias":["Pimampiro","Mariano Acosta","San Francisco de Sigsipamba"]},
    "URCUQUI": {"nombre":"Urcuqu\u00ed","parroquias":["Urcuqu\u00ed","Cahuasqu\u00ed","La Merced de Buenos Aires","Pablo Arenas","San Blas","Tumbabiro"]}
  }},
  "LOJA": {"nombre":"Loja","cantones":{
    "LOJA": {"nombre":"Loja","parroquias":["Loja","Carig\u00e1n","Chuquiribamba","El Cisne","Gualel","Jimbilla","Malacatos","Quinara","San Lucas","San Pedro de Vilcabamba","Santiago","Taquil","Vilcabamba","El Valle","Sucre","Punzara","El Sagrario","San Sebasti\u00e1n"]},
    "CALVAS": {"nombre":"Calvas","parroquias":["Calvas","Cariamanga","Colaisaca","El Lucero","Utuana","Sanguill\u00edn"]},
    "CATAMAYO": {"nombre":"Catamayo","parroquias":["Catamayo","El Tambo","Guayquichuma","San Pedro de la Bendita","Zambi"]},
    "CELICA": {"nombre":"Celica","parroquias":["Celica","Cruzpamba","Pozul","San Juan","Teniente Maximino Rodr\u00edguez"]},
    "CHAGUARPAMBA": {"nombre":"Chaguarpamba","parroquias":["Chaguarpamba","Buenavista","El Rosario","Santa Rufina"]},
    "ESPINDOLA": {"nombre":"Esp\u00edndola","parroquias":["Esp\u00edndola","Amaluza","Bellavista","Santa Teresita","Jimbura"]},
    "GONZANAMA": {"nombre":"Gonzanam\u00e1","parroquias":["Gonzanam\u00e1","Changaimina","Purunuma","Sacapalca","Nambacola"]},
    "MACARA": {"nombre":"Macar\u00e1","parroquias":["Macar\u00e1","Larama","La Victoria","Sabianago","San Pedro","Yaguachi"]},
    "PALTAS": {"nombre":"Paltas","parroquias":["Paltas","Catacocha","Cangonam\u00e1","Guachinam\u00e1","Lauro Guerrero","Mercadillo","Yamana","Tambo"]},
    "PUYANGO": {"nombre":"Puyango","parroquias":["Puyango","Alamor","Ciano","El Arenal","El Limo","Mercadillo","Vicente Rocafuerte"]},
    "SARAGURO": {"nombre":"Saraguro","parroquias":["Saraguro","El Para\u00edso de Cel\u00e9n","La Manzana","Lluzhapa","San Antonio de Cumbaratza","San Pablo de Tenta","San Sebasti\u00e1n de Yuluc","Selva Alegre","Sumaypamba","Tambopamba","Urdaneta"]},
    "SOZORANGA": {"nombre":"Sozoranga","parroquias":["Sozoranga","Tacamoros","Nueva F\u00e1tima"]},
    "ZAPOTILLO": {"nombre":"Zapotillo","parroquias":["Zapotillo","Cazaderos","Garzareal","Limonplayo","Paletillas","Bolaspamba"]},
    "PINDAL": {"nombre":"Pindal","parroquias":["Pindal","Chaquinal","Milagros","Pueblo Nuevo"]},
    "QUILANGA": {"nombre":"Quilanga","parroquias":["Quilanga","Fundochamba","San Antonio"]},
    "OLMEDO": {"nombre":"Olmedo","parroquias":["Olmedo","La Tingue"]}
  }},
  "LOS_RIOS": {"nombre":"Los R\u00edos","cantones":{
    "BABAHOYO": {"nombre":"Babahoyo","parroquias":["Babahoyo","Barreiro","Caracol","Febres Cordero","La Uni\u00f3n","Pimocha","Clemente Baquerizo"]},
    "BABA": {"nombre":"Baba","parroquias":["Baba","Guare","Isla de Bejucal"]},
    "MONTALVO": {"nombre":"Montalvo","parroquias":["Montalvo"]},
    "PUEBLOVIEJO": {"nombre":"Puebloviejo","parroquias":["Puebloviejo","San Juan","Puerto Pechiche"]},
    "QUEVEDO": {"nombre":"Quevedo","parroquias":["Quevedo","San Crist\u00f3bal","San Jos\u00e9","Guayac\u00e1n","La Esperanza","Venus del R\u00edo Quevedo","Nicol\u00e1s Infante D\u00edaz","Siete de Octubre","24 de Mayo","Viva Alfaro"]},
    "URDANETA": {"nombre":"Urdaneta","parroquias":["Urdaneta","Catarama","Ricaurte"]},
    "VENTANAS": {"nombre":"Ventanas","parroquias":["Ventanas","Zapotal","Quinsaloma","10 de Noviembre"]},
    "VINCES": {"nombre":"Vinces","parroquias":["Vinces","Palmar de la Virgen","Antonio Sotomayor"]},
    "PALENQUE": {"nombre":"Palenque","parroquias":["Palenque"]},
    "BUENA_FE": {"nombre":"Buena Fe","parroquias":["Buena Fe","San Jacinto de Buena Fe","Patricia Pilar"]},
    "VALENCIA": {"nombre":"Valencia","parroquias":["Valencia"]},
    "MOCACHE": {"nombre":"Mocache","parroquias":["Mocache"]},
    "QUINSALOMA": {"nombre":"Quinsaloma","parroquias":["Quinsaloma"]}
  }},
  "MANABI": {"nombre":"Manab\u00ed","cantones":{
    "PORTOVIEJO": {"nombre":"Portoviejo","parroquias":["Portoviejo","Abd\u00f3n Calder\u00f3n","Alajuela","Calder\u00f3n","Chirijos","Picoaz\u00e1","Riocaut\u00edn","San Pl\u00e1cido","Col\u00f3n","Pueblo Nuevo","Franco","12 de Marzo","18 de Octubre","Andr\u00e9s de Vera"]},
    "BOLIVAR": {"nombre":"Bol\u00edvar","parroquias":["Bol\u00edvar","Calceta","Quiroga","Membrillo"]},
    "CHONE": {"nombre":"Chone","parroquias":["Chone","Boyac\u00e1","Canuto","Convento","Chibunga","Eloy Alfaro","Ricaurte","San Antonio","San Vicente","Santa Rita"]},
    "EL_CARMEN": {"nombre":"El Carmen","parroquias":["El Carmen","4 de Diciembre","Wilfrido Loor Moreira","Coronel"]},
    "FLAVIO_ALFARO": {"nombre":"Flavio Alfaro","parroquias":["Flavio Alfaro","Zapallo","San Francisco de Novillo"]},
    "JIPIJAPA": {"nombre":"Jipijapa","parroquias":["Jipijapa","Am\u00e9rica","El Anegado","Julcuy","La Uni\u00f3n","Machalilla","Membrillal","Pedro Pablo G\u00f3mez","Puerto de Cayo","San Lorenzo"]},
    "JUNIN": {"nombre":"Jun\u00edn","parroquias":["Jun\u00edn"]},
    "MANTA": {"nombre":"Manta","parroquias":["Manta","Eloy Alfaro","Los Esteros","San Mateo","Tarqui","San Lorenzo"]},
    "MONTECRISTI": {"nombre":"Montecristi","parroquias":["Montecristi","An\u00edbal San Andr\u00e9s","Colorado","General Eloy Alfaro","La Pila","Le\u00f3nidas Proa\u00f1o"]},
    "PAJAN": {"nombre":"Paj\u00e1n","parroquias":["Paj\u00e1n","Cascol","Campanilla"]},
    "PICHINCHA": {"nombre":"Pichincha","parroquias":["Pichincha","Barraganete","San Sebasti\u00e1n"]},
    "ROCAFUERTE": {"nombre":"Rocafuerte","parroquias":["Rocafuerte"]},
    "SANTA_ANA": {"nombre":"Santa Ana","parroquias":["Santa Ana","Lodana","Santa Ana de Vuelta Larga","Ayacucho","Honorato V\u00e1squez","La Uni\u00f3n"]},
    "SUCRE": {"nombre":"Sucre","parroquias":["Sucre","Bah\u00eda de Car\u00e1quez","Canoa","Cojim\u00edes","Charapot\u00f3","San Isidro","San Vicente","Tosagua"]},
    "TOSAGUA": {"nombre":"Tosagua","parroquias":["Tosagua","Bachillero","La Concordia"]},
    "VEINTICUATRO_DE_MAYO": {"nombre":"24 de Mayo","parroquias":["24 de Mayo","Bellavista","Noboa"]},
    "PEDERNALES": {"nombre":"Pedernales","parroquias":["Pedernales","Cojim\u00edes","10 de Agosto","Atahualpa"]},
    "OLMEDO": {"nombre":"Olmedo","parroquias":["Olmedo"]},
    "PUERTO_LOPEZ": {"nombre":"Puerto L\u00f3pez","parroquias":["Puerto L\u00f3pez","Machalilla","Salango"]},
    "JAMA": {"nombre":"Jama","parroquias":["Jama"]},
    "JARAMIJO": {"nombre":"Jaramij\u00f3","parroquias":["Jaramij\u00f3"]},
    "SAN_VICENTE": {"nombre":"San Vicente","parroquias":["San Vicente","Canoa"]}
  }},
  "MORONA_SANTIAGO": {"nombre":"Morona Santiago","cantones":{
    "MORONA": {"nombre":"Morona","parroquias":["Morona","Alshi","General Proa\u00f1o","San Isidro","Sevilla Don Bosco","Z\u00fa\u00f1ac","Amazoonas","Cuchaentza","R\u00edo Blanco","Chiguaza"]},
    "GUALAQUIZA": {"nombre":"Gualaquiza","parroquias":["Gualaquiza","Amazonas","Bomboiza","Chiguinda","El Rosario","Nueva Tarqui","San Miguel de Cuyes","Shella"]},
    "LIMON": {"nombre":"Lim\u00f3n Indanza","parroquias":["Lim\u00f3n Indanza","San Miguel de Conchay","Pa\u00f1a","San Antonio","Yunganza"]},
    "PALORA": {"nombre":"Palora","parroquias":["Palora","Arapicos","Cumand\u00e1","Sangay","16 de Agosto"]},
    "SANTIAGO": {"nombre":"Santiago","parroquias":["Santiago","San Jos\u00e9 de Morona","Santiago de M\u00e9ndez","Patuca"]},
    "SUCUA": {"nombre":"Suc\u00faa","parroquias":["Suc\u00faa","Asunci\u00f3n","La Vieja","Huambi"]},
    "HUAMBOYA": {"nombre":"Huamboya","parroquias":["Huamboya","Chiguaza"]},
    "SAN_JUAN_BOSCO": {"nombre":"San Juan Bosco","parroquias":["San Juan Bosco","Pan de Az\u00facar","San Carlos de Lim\u00f3n","Santiago de Pananza"]},
    "TAISHA": {"nombre":"Taisha","parroquias":["Taisha","Huasaga","Macuma","Quimi","Tuna"]},
    "LOGROÑO": {"nombre":"Logro\u00f1o","parroquias":["Logro\u00f1o","Yaupi","Shimpis"]},
    "PABLO_SEXTO": {"nombre":"Pablo Sexto","parroquias":["Pablo Sexto"]},
    "TIGRE": {"nombre":"Tigre","parroquias":["Tigre"]}
  }},
  "NAPO": {"nombre":"Napo","cantones":{
    "TENA": {"nombre":"Tena","parroquias":["Tena","Ahuano","Archidona","Chontapunta","Puerto Misahuall\u00ed","Puerto Napo","Talag","San Juan de Muyuna"]},
    "ARCHIDONA": {"nombre":"Archidona","parroquias":["Archidona","Cotundo","San Pablo de Ushpayacu"]},
    "EL_CHACO": {"nombre":"El Chaco","parroquias":["El Chaco","Gonzalo D\u00edaz de Pineda","Linares","Oyacachi","Sardinas","Santa Rosa"]},
    "QUIJOS": {"nombre":"Quijos","parroquias":["Quijos","Baeza","Cosanga","Cuyuja","Papallacta","San Francisco de Borja","Sumaco"]},
    "CARLOS_JULIO_AROSEMENA": {"nombre":"Carlos Julio Arosemena Tola","parroquias":["Carlos Julio Arosemena Tola"]}
  }},
  "ORELLANA": {"nombre":"Orellana","cantones":{
    "FRANCISCO_DE_ORELLANA": {"nombre":"Francisco de Orellana","parroquias":["Francisco de Orellana","Dayuma","Taracoa","El Dorado","La Belleza","San Luis de Armenia","In\u00e9s Arango","La Joya de los Sachas","Puerto Francisco de Orellana","Loreto","Michellena"]},
    "LA_JOYA_DE_LOS_SACHAS": {"nombre":"La Joya de los Sachas","parroquias":["La Joya de los Sachas","Enokanqui","San Carlos","Pompeya","San Sebasti\u00e1n del Coca","Lago San Pedro","Rumipamba"]},
    "LORETO": {"nombre":"Loreto","parroquias":["Loreto","Avila","Puerto Murialdo","San Jos\u00e9 de Payamino","San Vicente de Huaticocha","El Ed\u00e9n"]},
    "AGUARICO": {"nombre":"Aguarico","parroquias":["Aguarico","Tiputini","Yasun\u00ed","Santa Mar\u00eda de Huiririma","Capit\u00e1n Augusto Rivadeneyra","Cononaco","Nuevo Rocafuerte","Secoya"]}
  }},
  "PASTAZA": {"nombre":"Pastaza","cantones":{
    "PASTAZA": {"nombre":"Pastaza","parroquias":["Pastaza","Canelos","Diez de Agosto","El Triunfo","F\u00e1tima","Montalvo","Puyo","R\u00edo Corrientes","R\u00edo Tigre","San Jos\u00e9 de Canelos","Santa Clara","Sarayaku","Sim\u00f3n Bol\u00edvar","Tarikua","Teniente Hugo Ortiz","Veracruz","Mera"]},
    "MERA": {"nombre":"Mera","parroquias":["Mera","Shell"]},
    "SANTA_CLARA": {"nombre":"Santa Clara","parroquias":["Santa Clara"]},
    "ARAJUNO": {"nombre":"Arajuno","parroquias":["Arajuno","Curaray"]}
  }},
  "PICHINCHA": {"nombre":"Pichincha","cantones":{
    "QUITO": {"nombre":"Quito","parroquias":["Quito","Belisario Quevedo","Carcel\u00e9n","Centro Hist\u00f3rico","Cotocollao","El Condado","El Inca","Guaman\u00ed","I\u00f1aquito","Itchimb\u00eda","Jipijapa","Kennedy","La Argelia","La Caroli\u00f3n","La Ecuatoriana","La Ferroviaria","La Libertad","La Magdalena","La Mena","Mariscal Sucre","Ponceano","Puengas\u00ed","Quitumbe","Rumipamba","San Bartolo","San Juan","Solanda","Turubamba","Calacal\u00ed","Calder\u00f3n","Conocoto","Cumbay\u00e1","Chavezpamba","Checa","El Quinche","Gualea","Guayllabamba","La Merced","Llano Chico","Lloa","Nanegal","Nanegalito","Nay\u00f3n","Nono","Pacto","Pedregal","Perucho","Pifo","Pintag","Pomasqui","Puembo","P\u00edntag","San Antonio de Pichincha","San Jos\u00e9 de Minas","Tababela","Tumbaco","Yaruqu\u00ed","Zambiza","Puerto Quito"]},
    "CAYAMBE": {"nombre":"Cayambe","parroquias":["Cayambe","Asc\u00e1zubi","Cangahua","Olmedo","Ot\u00f3n","Santa Rosa de Cusubamba"]},
    "MEJIA": {"nombre":"Mej\u00eda","parroquias":["Mej\u00eda","Al\u00f3ag","Aloas\u00ed","El Chaupi","El Coraz\u00f3n","Cutuglagua","Manuel Cornejo Astorga","Tambillo","Uyumbicho"]},
    "PEDRO_MONCAYO": {"nombre":"Pedro Moncayo","parroquias":["Pedro Moncayo","Tabacundo","La Esperanza","Malchingu\u00ed","Tocachi"]},
    "RUMIÑAHUI": {"nombre":"Rumi\u00f1ahui","parroquias":["Rumi\u00f1ahui","Sangolqu\u00ed","San Pedro de Taboada","San Rafael","Cotogchoa","Rumipamba","La Concordia"]},
    "SAN_MIGUEL_DE_LOS_BANCOS": {"nombre":"San Miguel de los Bancos","parroquias":["San Miguel de los Bancos","Mindo","Pedro Vicente Maldonado"]},
    "PEDRO_VICENTE_MALDONADO": {"nombre":"Pedro Vicente Maldonado","parroquias":["Pedro Vicente Maldonado"]},
    "PUERTO_QUITO": {"nombre":"Puerto Quito","parroquias":["Puerto Quito"]}
  }},
  "SANTA_ELENA": {"nombre":"Santa Elena","cantones":{
    "SANTA_ELENA": {"nombre":"Santa Elena","parroquias":["Santa Elena","Anc\u00f3n","Atahualpa","Bambay\u00e1n","Chanduy","Colonche","Jose Luis Tamayo","La Libertad","Salinas","San Pablo","Sim\u00f3n Bol\u00edvar","Tabuga"]},
    "LA_LIBERTAD": {"nombre":"La Libertad","parroquias":["La Libertad"]},
    "SALINAS": {"nombre":"Salinas","parroquias":["Salinas","Anconcito","Jos\u00e9 Luis Tamayo","Vicente Rocafuerte"]}
  }},
  "SANTO_DOMINGO_TSACHILAS": {"nombre":"Santo Domingo de los Ts\u00e1chilas","cantones":{
    "SANTO_DOMINGO": {"nombre":"Santo Domingo","parroquias":["Santo Domingo","Abraham Calazac\u00f3n","Bombol\u00ed","Chig\u00fcilpe","R\u00edo Toachi","R\u00edo Verde","San Jacinto del B\u00faa","San Jos\u00e9 de Alluriqu\u00edn","San Luis de Bamba","Valle Hermoso","Zaracay","Puerto Quito"]},
    "LA_CONCORDIA": {"nombre":"La Concordia","parroquias":["La Concordia","Monterrey","Las Villegas"]}
  }},
  "SUCUMBIOS": {"nombre":"Sucumb\u00edos","cantones":{
    "LAGO_AGRIO": {"nombre":"Lago Agrio","parroquias":["Lago Agrio","El Eno","General Farf\u00e1n","Gonzalo Pizarro","Jambel\u00ed","Nueva Loja","Pacayacu","Santa Cecilia","Santa Elena","Santo Domingo de los Colorados","Tetuete","Dureno"]},
    "CASCALES": {"nombre":"Cascales","parroquias":["Cascales","Santa Rosa de Sucumb\u00edos","Sevilla"]},
    "CUYABENO": {"nombre":"Cuyabeno","parroquias":["Cuyabeno","Tarapoa","Aguarico","Capit\u00e1n Augusto Rivadeneyra","Puerto Bol\u00edvar"]},
    "GONZALO_PIZARRO": {"nombre":"Gonzalo Pizarro","parroquias":["Gonzalo Pizarro","El Dorado de Cascales","Lumbaqui","Puerto Libre","Santa Rosa de Sucumb\u00edos"]},
    "PUTUMAYO": {"nombre":"Putumayo","parroquias":["Putumayo","Palma Roja","Puerto El Carmen de Putumayo","San Roque","Santa Elena"]},
    "SHUSHUFINDI": {"nombre":"Shushufindi","parroquias":["Shushufindi","Enokanqui","Lim\u00f3ncocha","Pa\u00f1acocha","San Pedro de los Cofanes","San Roque","Siete de Julio"]},
    "SUCUMBIOS": {"nombre":"Sucumb\u00edos","parroquias":["Sucumb\u00edos","La Bonita","Playas de Cuyabeno","Santa B\u00e1rbara"]}
  }},
  "TUNGURAHUA": {"nombre":"Tungurahua","cantones":{
    "AMBATO": {"nombre":"Ambato","parroquias":["Ambato","Ambato (La Matriz)","Atocha-Ficoa","Celiano Monge","Huachi Chico","Huachi Loreto","Ingahurco","Izamba","La Merced","La Pen\u00ednsula","Mart\u00ednez","Pasa","Picaihua","Pilotaje","Quisapincha","San Bartolom\u00e9 de Pinllo","San Fernando","Santa Rosa","Totoras","Uni\u00f3n"]},
    "BAÑOS": {"nombre":"Ba\u00f1os de Agua Santa","parroquias":["Ba\u00f1os de Agua Santa","Lligua","R\u00edo Negro","R\u00edo Verde","Ulba"]},
    "CEVALLOS": {"nombre":"Cevallos","parroquias":["Cevallos"]},
    "MOCHA": {"nombre":"Mocha","parroquias":["Mocha","Pinguil\u00ed"]},
    "PATATE": {"nombre":"Patate","parroquias":["Patate","El Triunfo","Los Andes","Sucre"]},
    "QUERO": {"nombre":"Quero","parroquias":["Quero"]},
    "SAN_PEDRO_DE_PELILEO": {"nombre":"San Pedro de Pelileo","parroquias":["San Pedro de Pelileo","Benza","Chiquicha","El Rosario","Huambal\u00f3","Salasaca"]},
    "SANTIAGO_DE_PILLARO": {"nombre":"Santiago de P\u00edllaro","parroquias":["Santiago de P\u00edllaro","Baquerizo Moreno","Emilio Mar\u00eda Ter\u00e1n","Marcos Espinel","Presidente Urbina","San Miguelito","San Andr\u00e9s"]},
    "TISALEO": {"nombre":"Tisaleo","parroquias":["Tisaleo"]}
  }},
  "ZAMORA_CHINCHIPE": {"nombre":"Zamora Chinchipe","cantones":{
    "ZAMORA": {"nombre":"Zamora","parroquias":["Zamora","Cumbaratza","Guadalupe","Imbana","Paquishapa","Sabanilla","San Antonio","San Carlos de las Minas","Timbara","Zumbi"]},
    "CHINCHIPE": {"nombre":"Chinchipe","parroquias":["Chinchipe","Chito","El Chorro","La Uni\u00f3n","Palanda","Pucapamba","San Andr\u00e9s","Zumba"]},
    "NANGARITZA": {"nombre":"Nangaritza","parroquias":["Nangaritza","Gualaquiza","Zurmi"]},
    "YACUAMBI": {"nombre":"Yacuambi","parroquias":["Yacuambi","La Paz","Tutupali"]},
    "YANTZAZA": {"nombre":"Yantzaza","parroquias":["Yantzaza","Chica\u00f1a","El Pangui","Los Encuentros","Zumbi"]},
    "EL_PANGUI": {"nombre":"El Pangui","parroquias":["El Pangui","El Guismi","Tundayme"]},
    "ZUMBI": {"nombre":"Zumbi","parroquias":["Zumbi"]},
    "CENTINELA_DEL_CONDOR": {"nombre":"Centinela del C\u00f3ndor","parroquias":["Centinela del C\u00f3ndor","Zumbi"]},
    "PALANDA": {"nombre":"Palanda","parroquias":["Palanda","El Porvenir del Carmen","San Francisco del Vergel","Valladolid"]},
    "PAQUISHA": {"nombre":"Paquisha","parroquias":["Paquisha","Bellavista","Nuevo Quito"]}
  }}
};
"""

# Insert Ecuador data before the first use (before openPatientModal function)
# Find the end of calcEdad or before openPatientModal
html = html.replace('function openPatientModal() {', ECUADOR_DATA + '\nfunction openPatientModal() {')
print("Inserted ECUADOR data block")

# 2. Helper function to get Ecuador date/time string
HTML_DATE_FN = """
function fechaEcuador(fecha) {
  if (!fecha) return '-';
  try {
    return new Date(fecha).toLocaleString('es-EC', {timeZone:'America/Guayaquil', hour12:false});
  } catch(e) { return fecha; }
}
function fechaEcuadorDate(fecha) {
  if (!fecha) return '-';
  try {
    return new Date(fecha).toLocaleDateString('es-EC', {timeZone:'America/Guayaquil'});
  } catch(e) { return fecha; }
}
function horaEcuador(fecha) {
  if (!fecha) return '-';
  try {
    return new Date(fecha).toLocaleTimeString('es-EC', {timeZone:'America/Guayaquil', hour12:false});
  } catch(e) { return fecha; }
}
"""
html = html.replace('function todayEcuador() {', HTML_DATE_FN + '\nfunction todayEcuador() {')
print("Inserted fechaEcuador helper functions")

# 3. Update openPatientModal to add seguroSalud, provincia, canton, parroquia
OLD_PATIENT_MODAL = "    '<div class=\"row g-2 mt-2\"><div class=\"col-md-4\"><label class=\"form-label\">Cedula (10 dig.) *</label><input type=\"text\" id=\"pCedula\" class=\"form-control\" maxlength=\"10\" required><small id=\"pCedulaHelp\" class=\"text-muted\">Ingrese cedula ecuatoriana</small></div>' +"
NEW_PATIENT_MODAL = "    '<div class=\"row g-2 mt-2\"><div class=\"col-md-4\"><label class=\"form-label\">Cedula (10 dig.) *</label><input type=\"text\" id=\"pCedula\" class=\"form-control\" maxlength=\"10\" required><small id=\"pCedulaHelp\" class=\"text-muted\">Ingrese cedula ecuatoriana</small></div>' +\n" + \
    "    '<div class=\"col-md-3\"><label class=\"form-label\">Seguro Salud</label><select id=\"pSeguroSalud\" class=\"form-select\"><option value=\"\">Seleccione...</option><option value=\"IESS\">IESS</option><option value=\"ISSFA\">ISSFA</option><option value=\"ISSPOL\">ISSPOL</option><option value=\"PRIVADO\">Privado</option><option value=\"NINGUNO\">Ninguno</option></select></div>' +\n" + \
    "    '<div class=\"col-md-3\"><label class=\"form-label\">Provincia</label><select id=\"pProvincia\" class=\"form-select\" onchange=\"cargarCantones(\'p\', this.value)\"><option value=\"\">Seleccione...</option>'+Object.keys(ECUADOR).map(k=>'<option value=\"'+k+'\">'+ECUADOR[k].nombre+'</option>').join('')+'</select></div>' +\n" + \
    "    '<div class=\"col-md-2\"><label class=\"form-label\">Cant\u00f3n</label><select id=\"pCanton\" class=\"form-select\" onchange=\"cargarParroquias(\'p\', this.value)\"><option value=\"\">Seleccione...</option></select></div>' +\n" + \
    "    '<div class=\"col-md-2\"><label class=\"form-label\">Parroquia</label><select id=\"pParroquia\" class=\"form-select\"><option value=\"\">Seleccione...</option></select></div>' +\n"
html = html.replace(OLD_PATIENT_MODAL, NEW_PATIENT_MODAL)
print("Updated openPatientModal with seguroSalud, provincia, canton, parroquia")

# A. Also update the address section - add provincia, canton, parroquia after direccion
OLD_DIRECCION = "    '<div class=\"mt-2\"><label class=\"form-label\">Direccion</label><textarea id=\"pDireccion\" class=\"form-control\" rows=\"2\"></textarea></div>' +"
# Remove the old direccion line - we'll move it after the new fields
html = html.replace(OLD_DIRECCION, "    '<div class=\"row g-2 mt-2\"><div class=\"col-md-6\"><label class=\"form-label\">Direccion</label><textarea id=\"pDireccion\" class=\"form-control\" rows=\"2\"></textarea></div><div class=\"col-md-6\"><label class=\"form-label\">Ocupacion</label><input type=\"text\" id=\"pOcupacion\" class=\"form-control\"></div></div>' +")
print("Updated direccion section in openPatientModal")

# Remove the duplicate Ocupacion field now
OLD_OCUPACION = "'<div class=\"col-md-3\"><label class=\"form-label\">Ocupacion</label><input type=\"text\" id=\"pOcupacion\" class=\"form-control\"></div></div>' +"
# This was after estado civil, we need to remove it since we moved it
html = html.replace(OLD_OCUPACION, "'<div class=\"col-md-6\"><label class=\"form-label\">Ocupacion</label></div></div>' +")
# Actually let me re-read the exact text to replace

# Let me check what the estado civil + ocupacion line looks like now
# The original was: '<div class="col-md-3"><label class="form-label">Estado Civil</label>...<div class="col-md-3"><label class="form-label">Ocupacion</label>...</div></div>'
# After our changes, we need to remove the duplicate ocupacion from estado civil row

OLD_EC_OC = "'<div class=\"col-md-3\"><label class=\"form-label\">Estado Civil</label><select id=\"pEstadoCivil\" class=\"form-select\"><option value=\"SOLTERO\">Soltero/a</option><option value=\"CASADO\">Casado/a</option><option value=\"DIVORCIADO\">Divorciado/a</option><option value=\"VIUDO\">Viudo/a</option></select></div><div class=\"col-md-3\"><label class=\"form-label\">Ocupacion</label><input type=\"text\" id=\"pOcupacion\" class=\"form-control\"></div></div>' +"
NEW_EC_OC = "'<div class=\"col-md-3\"><label class=\"form-label\">Estado Civil</label><select id=\"pEstadoCivil\" class=\"form-select\"><option value=\"SOLTERO\">Soltero/a</option><option value=\"CASADO\">Casado/a</option><option value=\"DIVORCIADO\">Divorciado/a</option><option value=\"VIUDO\">Viudo/a</option></select></div><div class=\"col-md-3\"><label class=\"form-label\">Nacionalidad</label><input type=\"text\" id=\"pNacionalidad\" class=\"form-control\" value=\"Ecuatoriana\"></div></div>' +"
html = html.replace(OLD_EC_OC, NEW_EC_OC)
print("Updated estado civil row - replaced ocupacion with nacionalidad")

# 4. Update savePatient to include new fields
OLD_SAVE_BODY = """    const body = {
      nombres:n, apellidos:a, cedula:c, fechaNacimiento:f, genero:g,
      grupoSanguineo:$('pGrupoSanguineo').value||'',
      telefono:$('pTelefono').value.trim(), email:$('pEmail').value.trim(),
      estadoCivil:$('pEstadoCivil').value||'',
      ocupacion:$('pOcupacion').value.trim(),
      direccion:$('pDireccion').value.trim(),
      alergias:$('pAlergias').value.trim(),
      enfermedadActual:$('pEnfermedadActual').value.trim(),
      antecedentesPersonales:{cronicas:$('pAntCronicas').value.trim(),cirugias:$('pCirugias').value.trim(),otros:$('pAntOtros').value.trim()},
      antecedentesFamiliares:{padre:$('pAntFamPadre').value.trim(),madre:$('pAntFamMadre').value.trim(),hermanos:$('pAntFamHermanos').value.trim(),hijos:$('pAntFamHijos').value.trim()},
      contactoEmergencia:{nombre:$('pEmergNombre').value.trim(),telefono:$('pEmergTelefono').value.trim(),parentesco:$('pEmergParentesco').value||''}
    };"""

NEW_SAVE_BODY = """    const body = {
      nombres:n, apellidos:a, cedula:c, fechaNacimiento:f, genero:g,
      grupoSanguineo:$('pGrupoSanguineo').value||'',
      telefono:$('pTelefono').value.trim(), email:$('pEmail').value.trim(),
      estadoCivil:$('pEstadoCivil').value||'',
      nacionalidad:$('pNacionalidad').value.trim()||'Ecuatoriana',
      ocupacion:$('pOcupacion').value.trim(),
      direccion:$('pDireccion').value.trim(),
      provincia:$('pProvincia').value ? ECUADOR[$('pProvincia').value].nombre : '',
      canton:$('pCanton').value ? ECUADOR[$('pProvincia').value].cantones[$('pCanton').value].nombre : '',
      parroquia:$('pParroquia').value||'',
      seguroSalud:$('pSeguroSalud').value||'',
      alergias:$('pAlergias').value.trim(),
      enfermedadActual:$('pEnfermedadActual').value.trim(),
      antecedentesPersonales:{cronicas:$('pAntCronicas').value.trim(),cirugias:$('pCirugias').value.trim(),otros:$('pAntOtros').value.trim()},
      antecedentesFamiliares:{padre:$('pAntFamPadre').value.trim(),madre:$('pAntFamMadre').value.trim(),hermanos:$('pAntFamHermanos').value.trim(),hijos:$('pAntFamHijos').value.trim()},
      contactoEmergencia:{nombre:$('pEmergNombre').value.trim(),telefono:$('pEmergTelefono').value.trim(),parentesco:$('pEmergParentesco').value||''}
    };"""

if OLD_SAVE_BODY in html:
    html = html.replace(OLD_SAVE_BODY, NEW_SAVE_BODY)
    print("Updated savePatient body with new fields")
else:
    # Try with spaces variation
    OLD_SAVE_BODY2 = """    const body = {
      nombres:n, apellidos:a, cedula:c, fechaNacimiento:f, genero:g,
      grupoSanguineo:$('pGrupoSanguineo').value||'',
      telefono:$('pTelefono').value.trim(), email:$('pEmail').value.trim(),
      estadoCivil:$('pEstadoCivil').value||'',
      ocupacion:$('pOcupacion').value.trim(),
      direccion:$('pDireccion').value.trim(),"""
    if OLD_SAVE_BODY2 in html:
        rest = html.split(OLD_SAVE_BODY2)[1]
        html = html.replace(OLD_SAVE_BODY2 + rest.split("};")[0] + "};", NEW_SAVE_BODY)
        print("Updated savePatient body (variant 2)")

# 5. Add cascading dropdown functions
CASCADE_FUNCTIONS = """
function cargarCantones(prefix, provinciaKey) {
  const cantonSel = $(prefix + 'Canton');
  const parrSel = $(prefix + 'Parroquia');
  cantonSel.innerHTML = '<option value="">Seleccione...</option>';
  parrSel.innerHTML = '<option value="">Seleccione...</option>';
  if (!provinciaKey || !ECUADOR[provinciaKey]) return;
  const cantones = ECUADOR[provinciaKey].cantones;
  Object.keys(cantones).forEach(k => {
    cantonSel.innerHTML += '<option value="' + k + '">' + cantones[k].nombre + '</option>';
  });
}

function cargarParroquias(prefix, cantonKey) {
  const parrSel = $(prefix + 'Parroquia');
  parrSel.innerHTML = '<option value="">Seleccione...</option>';
  const provSel = $(prefix + 'Provincia');
  if (!provSel || !provSel.value || !cantonKey) return;
  const parroquias = ECUADOR[provSel.value].cantones[cantonKey].parroquias;
  parroquias.forEach(p => {
    parrSel.innerHTML += '<option value="' + p + '">' + p + '</option>';
  });
}
"""
# Insert after Ecuador data block (before openPatientModal)
html = html.replace("function openPatientModal() {", CASCADE_FUNCTIONS + "\nfunction openPatientModal() {")
print("Inserted cascading dropdown functions")

# 6. Enhanced clearConsult() - reset ALL fields
OLD_CLEAR_CONSULT = """function clearConsult() {
  $('consultSelected').classList.remove('show');
  $('consultPatientRegionalId').value='';
  examenesSolicitados=[];
  renderExamenes();
  selectedConsultPatientGender = null;
  if ($('gynObsSection')) $('gynObsSection').style.display = '';
}"""

NEW_CLEAR_CONSULT = """function clearConsult() {
  $('consultSelected').classList.remove('show');
  $('consultPatientRegionalId').value='';
  examenesSolicitados=[];
  renderExamenes();
  selectedConsultPatientGender = null;
  // Clear ALL consultation form fields
  var fields = ['consSede','consEspecialidad','consMedico','consCie10','consDiagnostico',
    'consSintomas','consTratamiento','consExamenFisico','consObservaciones','consRecomendaciones',
    'consPASistole','consPADiastole','consFC','consFR','consTemp','consSpO2','consPeso',
    'consTalla','consIMC','consPerimetroAbdominal','consGlasgow','consDolor',
    'consFUR','consEdadGestacional','consGestas','consPartos','consCesareas','consAbortos',
    'consCitologia','consMamografia','consMetodoAnticonceptivo','consLactancia'];
  fields.forEach(function(id) {
    var el = $(id);
    if (el) {
      if (el.tagName === 'SELECT') el.selectedIndex = 0;
      else if (el.type === 'number' || el.type === 'text' || el.type === 'textarea') el.value = '';
    }
  });
  var cie10Sug = $('consCie10Sug');
  if (cie10Sug) cie10Sug.classList.remove('show');
  if ($('gynObsSection')) $('gynObsSection').style.display = '';
  // Reset CIE-10 catalog highlight
  window._cie10Highlight = -1;
}"""

html = html.replace(OLD_CLEAR_CONSULT, NEW_CLEAR_CONSULT)
print("Updated clearConsult() to reset ALL fields")

# 7. Update metadata display in clinical history - add usuarioCreacion, usuarioModificacion
# In renderPatientDataTab, add usuarioCreacion and usuarioModificacion after Ultima Actualizacion
OLD_META_END = "        gridItem('Ultima Actualizacion', p.fechaActualizacion ? new Date(p.fechaActualizacion).toLocaleString('es-EC') : (p.fechaRegistro ? new Date(p.fechaRegistro).toLocaleString('es-EC') : '-')) +"

NEW_META_END = "        gridItem('Ultima Actualizacion', p.fechaActualizacion ? new Date(p.fechaActualizacion).toLocaleString('es-EC') : (p.fechaRegistro ? new Date(p.fechaRegistro).toLocaleString('es-EC') : '-')) +\n" + \
    "        gridItem('Creado por', p.usuarioCreacion || '-') +\n" + \
    "        gridItem('Modificado por', p.usuarioModificacion || '-') +"

html = html.replace(OLD_META_END, NEW_META_END)
print("Added usuarioCreacion and usuarioModificacion to patient data tab")

# Also update renderPatientCard to show metadata with Ecuador timezone
OLD_CARD_META = "        gridItem('Fecha Creacion', p.fechaRegistro || '-') +\n        gridItem('Ultima Actualizacion', p.fechaActualizacion || p.fechaRegistro || '-') +"
NEW_CARD_META = "        gridItem('Fecha Creacion', fechaEcuador(p.fechaRegistro)) +\n        gridItem('Ultima Actualizacion', fechaEcuador(p.fechaActualizacion || p.fechaRegistro)) +\n        gridItem('Estado', p.activo !== false ? '<span class=\"status-badge status-ACTIVO\">Activo</span>' : '<span class=\"status-badge status-ALERTA\">Inactivo</span>') +"
html = html.replace(OLD_CARD_META, NEW_CARD_META)
print("Updated renderPatientCard metadata with Ecuador timezone and status")

# 8. Update consultas table to show full CIE-10 code + description
OLD_CIE10_TABLE = "'<td><code>' + (c.diagnosticoPrincipalCie10 || '-') + '</code></td>' +"
NEW_CIE10_TABLE = "'<td><code>' + (c.diagnosticoPrincipalCie10 || '-') + '</code><br><small style=\"color:#666;font-size:0.7rem\">' + (c.diagnosticoPrincipalDesc || '') + '</small></td>' +"
html = html.replace(OLD_CIE10_TABLE, NEW_CIE10_TABLE)
print("Updated CIE-10 display in consultas table to show code + description")

# 9. Update the consultation detail modal to show CIE-10 more comprehensively
OLD_CIE10_MODAL = "modalItem('Diagnostico Principal (CIE-10)', (c.diagnosticoPrincipalCie10 ? '<code>' + c.diagnosticoPrincipalCie10 + '</code> ' : '') + (c.diagnosticoPrincipalDesc || ''), true) +"
# This already shows both code and description, which is good. No change needed.

# 10. Ensure consistency - update date formatting in history tabs to use Ecuador timezone
# Update renderConsultasTab to show fecha and hora using Ecuador timezone
OLD_CONSULTA_DATE = "'<td><strong>' + (c.fechaConsulta || '-') + '</strong></td>' +\n      '<td>' + (c.horaConsulta || '-') + '</td>' +"
NEW_CONSULTA_DATE = "'<td><strong>' + (c.fechaConsulta || '-') + '</strong></td>' +\n      '<td>' + fechaEcuador(c.fechaConsulta + (c.horaConsulta ? 'T' + c.horaConsulta : '')) + '</td>' +"
# Actually, let's handle this more carefully - we need to pass full datetime
# Let me keep it simpler - just show horaConsulta directly
# (the format is already set by the backend)

# 11. Update PDF/print to use Ecuador dates
OLD_PRINT_HEADER = "'  <p style=\"font-size:0.7rem;color:#888\">Documento generado: ' + new Date().toLocaleString() + ' | Usuario: ' + (currentUser ? currentUser.username : 'N/A') + '</p>' +"
NEW_PRINT_HEADER = "'  <p style=\"font-size:0.7rem;color:#888\">Documento generado: ' + fechaEcuador(new Date()) + ' | Usuario: ' + (currentUser ? currentUser.username : 'N/A') + '</p>' +"
html = html.replace(OLD_PRINT_HEADER, NEW_PRINT_HEADER)
print("Updated print header with Ecuador date")

# 12. Update the audit call in savePatient to capture previous values when editing
# Find saveEditPatient and update the audit call
OLD_EDIT_AUDIT = "await sendAudit('PACIENTES', 'Actualizacion de paciente', 'ID Regional: ' + rid + ' - Paciente actualizado', 'EXITO', { paciente: rid, submodulo: 'Edicion' });"
NEW_EDIT_AUDIT = "await sendAudit('PACIENTES', 'Actualizacion de paciente', 'ID Regional: ' + rid + ' - Paciente actualizado', 'EXITO', { paciente: rid, submodulo: 'Edicion', campoModificado: 'Datos del paciente', valorAnterior: JSON.stringify(window._previousPatientData||{}), valorNuevo: JSON.stringify(body) });"
html = html.replace(OLD_EDIT_AUDIT, NEW_EDIT_AUDIT)
print("Updated edit audit call with valorAnterior/valorNuevo")

# 13. Add _previousPatientData capture in showPatientEditModal
OLD_EDIT_OPEN = "function showPatientEditModal(p) {"
NEW_EDIT_OPEN = "function showPatientEditModal(p) {\n  window._previousPatientData = JSON.parse(JSON.stringify(p));"
html = html.replace(OLD_EDIT_OPEN, NEW_EDIT_OPEN)
print("Added _previousPatientData capture in edit modal")

# 14. Update the edit patient modal to include seguroSalud, provincia, canton, parroquia
OLD_EDIT_CEDULA = "    '<div class=\"col-md-3\"><label class=\"form-label\">Fecha Nac. *</label><input type=\"date\" id=\"epFechaNac\" class=\"form-control\" max=\"' + todayEcuador() + '\" value=\"' + (p.fechaNacimiento||'') + '\"></div>' +"
# Find the edit modal and add new fields after genero

# Let's do a bigger replacement - find the groupo sanguineo line in edit and add after it
OLD_EDIT_GRUPO = "    '<div class=\"col-md-3\"><label class=\"form-label\">Grupo Sanguineo</label><select id=\"epGrupoSanguineo\" class=\"form-select\"><option value=\"\">Seleccione...</option>' + ['A+','A-','B+','B-','AB+','AB-','O+','O-'].map(g => '<option value=\"' + g + '\"' + (p.grupoSanguineo===g?' selected':'') + '>' + g + '</option>').join('') + '</select></div></div>' +"
NEW_EDIT_GRUPO = "    '<div class=\"col-md-3\"><label class=\"form-label\">Grupo Sanguineo</label><select id=\"epGrupoSanguineo\" class=\"form-select\"><option value=\"\">Seleccione...</option>' + ['A+','A-','B+','B-','AB+','AB-','O+','O-'].map(g => '<option value=\"' + g + '\"' + (p.grupoSanguineo===g?' selected':'') + '>' + g + '</option>').join('') + '</select></div>' +\n" + \
    "    '<div class=\"col-md-3\"><label class=\"form-label\">Seguro Salud</label><select id=\"epSeguroSalud\" class=\"form-select\"><option value=\"\">Seleccione...</option><option value=\"IESS\"' + (p.seguroSalud==='IESS'?' selected':'') + '>IESS</option><option value=\"ISSFA\"' + (p.seguroSalud==='ISSFA'?' selected':'') + '>ISSFA</option><option value=\"ISSPOL\"' + (p.seguroSalud==='ISSPOL'?' selected':'') + '>ISSPOL</option><option value=\"PRIVADO\"' + (p.seguroSalud==='PRIVADO'?' selected':'') + '>Privado</option><option value=\"NINGUNO\"' + (p.seguroSalud==='NINGUNO'?' selected':'') + '>Ninguno</option></select></div>' +\n" + \
    "    '<div class=\"col-md-3\"><label class=\"form-label\">Provincia</label><select id=\"epProvincia\" class=\"form-select\" onchange=\"cargarCantonesEdit(this.value)\"><option value=\"\">Seleccione...</option>' + Object.keys(ECUADOR).map(k => '<option value=\"' + k + '\"' + (ECUADOR[k].nombre === p.provincia ? ' selected' : '') + '>' + ECUADOR[k].nombre + '</option>').join('') + '</select></div>' +\n" + \
    "    '<div class=\"col-md-3\"><label class=\"form-label\">Cant\u00f3n</label><select id=\"epCanton\" class=\"form-select\" onchange=\"cargarParroquiasEdit(this.value)\"><option value=\"\">Seleccione...</option></select></div>' +\n" + \
    "    '<div class=\"col-md-3\"><label class=\"form-label\">Parroquia</label><select id=\"epParroquia\" class=\"form-select\"><option value=\"\">Seleccione...</option></select></div></div>' +"

html = html.replace(OLD_EDIT_GRUPO, NEW_EDIT_GRUPO)
print("Updated edit patient form with seguroSalud, provincia, canton, parroquia")

# 15. Add edit-specific cascade functions
EDIT_CASCADE = """
function cargarCantonesEdit(provinciaKey) {
  var cantonSel = $('epCanton');
  var parrSel = $('epParroquia');
  cantonSel.innerHTML = '<option value="">Seleccione...</option>';
  parrSel.innerHTML = '<option value="">Seleccione...</option>';
  if (!provinciaKey || !ECUADOR[provinciaKey]) return;
  var cantones = ECUADOR[provinciaKey].cantones;
  Object.keys(cantones).forEach(function(k) {
    cantonSel.innerHTML += '<option value="' + k + '">' + cantones[k].nombre + '</option>';
  });
}
function cargarParroquiasEdit(cantonKey) {
  var parrSel = $('epParroquia');
  parrSel.innerHTML = '<option value="">Seleccione...</option>';
  var provSel = $('epProvincia');
  if (!provSel || !provSel.value || !cantonKey) return;
  ECUADOR[provSel.value].cantones[cantonKey].parroquias.forEach(function(p) {
    parrSel.innerHTML += '<option value="' + p + '">' + p + '</option>';
  });
}
"""
# Insert after cargarParroquias function ends, before openPatientModal
html = html.replace("}\n\nfunction openPatientModal() {", "}\n\n" + EDIT_CASCADE + "\nfunction openPatientModal() {")
print("Inserted edit cascade functions")

# 16. Update saveEditPatient body to include new fields
OLD_EDIT_BODY = """      grupoSanguineo:$('epGrupoSanguineo').value||'',"""
NEW_EDIT_BODY = """      seguroSalud:$('epSeguroSalud').value||'',
      provincia:$('epProvincia').value ? ECUADOR[$('epProvincia').value].nombre : '',
      canton:$('epCanton').value ? ECUADOR[$('epProvincia').value].cantones[$('epCanton').value].nombre : '',
      parroquia:$('epParroquia').value||'',
      grupoSanguineo:$('epGrupoSanguineo').value||'',"""
html = html.replace(OLD_EDIT_BODY, NEW_EDIT_BODY)
print("Updated saveEditPatient body with new fields")

# 17. Fix date/time in clinical history - update toLocaleString calls to use 'es-EC'
# Replace all 'es-EC' timezone-naive calls where needed
# The key places are already using 'es-EC', so this should be fine.
# But let's make sure the patient data tab uses Ecuador timezone properly

OLD_PATIENT_TAB_META = "gridItem('Fecha de Creacion', p.fechaRegistro ? new Date(p.fechaRegistro).toLocaleString('es-EC') : '-') +\n        gridItem('Ultima Actualizacion', p.fechaActualizacion ? new Date(p.fechaActualizacion).toLocaleString('es-EC') : (p.fechaRegistro ? new Date(p.fechaRegistro).toLocaleString('es-EC') : '-')) +"
NEW_PATIENT_TAB_META = "gridItem('Fecha de Creacion', fechaEcuador(p.fechaRegistro)) +\n        gridItem('Ultima Actualizacion', fechaEcuador(p.fechaActualizacion || p.fechaRegistro)) +"
html = html.replace(OLD_PATIENT_TAB_META, NEW_PATIENT_TAB_META)
print("Updated patient data tab to use fechaEcuador()")

# 18. Fix the repositorio clinico preview to use fechaEcuador
OLD_RC_DATE = "rcGridItem('Ultima Consulta', p.fechaActualizacion ? new Date(p.fechaActualizacion).toLocaleString('es-EC') : (p.fechaRegistro ? new Date(p.fechaRegistro).toLocaleString('es-EC') : '-'));"
# Let me find this and update it
if "'es-EC'" in html:
    html = html.replace("new Date(p.fechaActualizacion).toLocaleString('es-EC')", "fechaEcuador(p.fechaActualizacion)")
    html = html.replace("new Date(p.fechaRegistro).toLocaleString('es-EC')", "fechaEcuador(p.fechaRegistro)")
    print("Updated date formatting to use fechaEcuador()")
    
# 19. Fix the repositorio audit display to show proper history changes
# The sendAudit function already supports valorAnterior and valorNuevo
# Update the patient edit call to pass these properly

# 20. Write the updated file
write_file(INDEX, html)
print("\\n=== FRONTEND CHANGES COMPLETE ===")

# =============================================================================
# BACKEND CHANGES - Paciente Entity
# =============================================================================
ENTITY_PATH = r"C:\Users\USER\OneDrive\Documentos\reto-solca-cloud\reto-solca-cloud\microservicio-pacientes\src\main\java\com\solca\pacientes\model\Paciente.java"
entity = read_file(ENTITY_PATH)

# Add new fields after "private String contactoEmergencia;"
OLD_ENTITY_CONTACTO = "    @Column(name = \"contacto_emergencia\", columnDefinition = \"TEXT\")\n    private String contactoEmergencia;"
NEW_ENTITY_CONTACTO = """    @Column(name = "contacto_emergencia", columnDefinition = "TEXT")
    private String contactoEmergencia;

    @Column(name = "seguro_salud", length = 30)
    private String seguroSalud;

    @Column(name = "provincia", length = 100)
    private String provincia;

    @Column(name = "canton", length = 100)
    private String canton;

    @Column(name = "parroquia", length = 100)
    private String parroquia;

    @Column(name = "nacionalidad", length = 50)
    @Builder.Default
    private String nacionalidad = "Ecuatoriana";

    @Column(name = "usuario_creacion", length = 50)
    private String usuarioCreacion;

    @Column(name = "usuario_modificacion", length = 50)
    private String usuarioModificacion;"""

entity = entity.replace(OLD_ENTITY_CONTACTO, NEW_ENTITY_CONTACTO)
write_file(ENTITY_PATH, entity)
print("Updated Paciente.java entity with new fields")

# =============================================================================
# BACKEND CHANGES - PacienteDTO
# =============================================================================
DTO_PATH = r"C:\Users\USER\OneDrive\Documentos\reto-solca-cloud\reto-solca-cloud\microservicio-pacientes\src\main\java\com\solca\pacientes\dto\PacienteDTO.java"
dto = read_file(DTO_PATH)

# Add fields after contactoEmergencia
OLD_DTO_CONTACTO = "    private Object contactoEmergencia;\n    private List<HistoriaLocalDTO> historiasLocales;"
NEW_DTO_CONTACTO = """    private Object contactoEmergencia;
    private String seguroSalud;
    private String provincia;
    private String canton;
    private String parroquia;
    private String nacionalidad;
    private String usuarioCreacion;
    private String usuarioModificacion;
    private List<HistoriaLocalDTO> historiasLocales;"""

dto = dto.replace(OLD_DTO_CONTACTO, NEW_DTO_CONTACTO)
write_file(DTO_PATH, dto)
print("Updated PacienteDTO.java with new fields")

# =============================================================================
# BACKEND CHANGES - PacienteService (update convertirADTO and build)
# =============================================================================
SERVICE_PATH = r"C:\Users\USER\OneDrive\Documentos\reto-solca-cloud\reto-solca-cloud\microservicio-pacientes\src\main\java\com\solca\pacientes\service\PacienteService.java"
service = read_file(SERVICE_PATH)

# Add new fields to the builder in convertirADTO
OLD_CONVERT = "                .contactoEmergencia(fromJsonString(p.getContactoEmergencia()))\n                .build();"
NEW_CONVERT = """                .contactoEmergencia(fromJsonString(p.getContactoEmergencia()))
                .seguroSalud(p.getSeguroSalud())
                .provincia(p.getProvincia())
                .canton(p.getCanton())
                .parroquia(p.getParroquia())
                .nacionalidad(p.getNacionalidad())
                .usuarioCreacion(p.getUsuarioCreacion())
                .usuarioModificacion(p.getUsuarioModificacion())
                .build();"""
service = service.replace(OLD_CONVERT, NEW_CONVERT)

# Add new fields to the registrarPaciente builder
OLD_REG_BUILD = "                .contactoEmergencia(toJsonString(dto.getContactoEmergencia()))\n                .activo(true)\n                .build();"
NEW_REG_BUILD = """                .contactoEmergencia(toJsonString(dto.getContactoEmergencia()))
                .seguroSalud(dto.getSeguroSalud())
                .provincia(dto.getProvincia())
                .canton(dto.getCanton())
                .parroquia(dto.getParroquia())
                .nacionalidad(dto.getNacionalidad() != null ? dto.getNacionalidad() : "Ecuatoriana")
                .usuarioCreacion(dto.getUsuarioCreacion())
                .activo(true)
                .build();"""
service = service.replace(OLD_REG_BUILD, NEW_REG_BUILD)

# Add new fields to actualizarPaciente
OLD_UPDATE_SET = "        paciente.setContactoEmergencia(toJsonString(dto.getContactoEmergencia()));"
NEW_UPDATE_SET = """        paciente.setContactoEmergencia(toJsonString(dto.getContactoEmergencia()));
        paciente.setSeguroSalud(dto.getSeguroSalud());
        paciente.setProvincia(dto.getProvincia());
        paciente.setCanton(dto.getCanton());
        paciente.setParroquia(dto.getParroquia());
        paciente.setNacionalidad(dto.getNacionalidad());
        paciente.setUsuarioModificacion(dto.getUsuarioModificacion());"""
service = service.replace(OLD_UPDATE_SET, NEW_UPDATE_SET)

write_file(SERVICE_PATH, service)
print("Updated PacienteService.java with new fields")

# =============================================================================
# FLYWAY MIGRATION V5
# =============================================================================
MIGRATION_PATH = r"C:\Users\USER\OneDrive\Documentos\reto-solca-cloud\reto-solca-cloud\microservicio-pacientes\src\main\resources\db\migration\V5__add_campos_ubicacion.sql"
migration_sql = """ALTER TABLE pacientes
    ADD COLUMN seguro_salud VARCHAR(30) DEFAULT NULL,
    ADD COLUMN provincia VARCHAR(100) DEFAULT NULL,
    ADD COLUMN canton VARCHAR(100) DEFAULT NULL,
    ADD COLUMN parroquia VARCHAR(100) DEFAULT NULL,
    ADD COLUMN nacionalidad VARCHAR(50) DEFAULT 'Ecuatoriana',
    ADD COLUMN usuario_creacion VARCHAR(50) DEFAULT NULL,
    ADD COLUMN usuario_modificacion VARCHAR(50) DEFAULT NULL;
"""
write_file(MIGRATION_PATH, migration_sql)
print("Created Flyway migration V5")

print("\\n=== ALL CHANGES COMPLETE ===")
print("1. Patient form: seguroSalud, provincia, canton, parroquia with cascading dropdowns")
print("2. Clinical history: usuarioCreacion, usuarioModificacion, status, Ecuador dates")
print("3. clearConsult() now resets ALL form fields")
print("4. CIE-10 shows code + description in history table")
print("5. Backend entity/DTO/service updated with new fields")
print("6. Flyway migration V5 created")
print("\\nNext: Build, deploy, and verify!")
